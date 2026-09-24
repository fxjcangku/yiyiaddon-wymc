package com.yiyiaddon.wymc.launcher.process;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinecraftProcessDetector {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftProcessDetector.class);
    
    // 网易 Minecraft 特有标识
    private static final String[] NETEASE_REQUIRED = {
        "netease"  // 必须包含网易标识
    };
    
    private static final String[] NETEASE_INDICATORS = {
        "MCLauncher", "mc-launcher", "网易"
    };
    
    // 排除标识（PCL、HMCL 等其他启动器）
    private static final String[] EXCLUDE_INDICATORS = {
        "PCL", "PCL2", "HMCL", "BakaXL", "MultiMC", 
        "fabric-loader", "forge", "quilt",  // 排除你的开发环境
        "yiyiaddon", "dev", "idea", "eclipse"  // 排除开发环境
    };
    
    public List<ProcessCandidate> detectMinecraftProcesses() {
        LOGGER.info("开始扫描 Minecraft 进程");
        
        List<ProcessCandidate> candidates = new ArrayList<>();
        
        if (Platform.isWindows()) {
            candidates = detectWindowsProcesses();
        } else {
            LOGGER.warn("当前仅支持 Windows 平台");
        }
        
        candidates.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        
        LOGGER.info("找到 {} 个候选进程", candidates.size());
        return candidates;
    }
    
    private List<ProcessCandidate> detectWindowsProcesses() {
        List<ProcessCandidate> candidates = new ArrayList<>();
        
        WinNT.HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(
            Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));
        
        try {
            Tlhelp32.PROCESSENTRY32.ByReference pe = new Tlhelp32.PROCESSENTRY32.ByReference();
            
            if (Kernel32.INSTANCE.Process32First(snapshot, pe)) {
                do {
                    String exeName = new String(pe.szExeFile).trim();
                    
                    if (exeName.toLowerCase().contains("java")) {
                        long pid = pe.th32ProcessID.longValue();
                        ProcessCandidate candidate = analyzeJavaProcess(pid, exeName);
                        if (candidate != null && candidate.getScore() > 0) {
                            candidates.add(candidate);
                        }
                    }
                } while (Kernel32.INSTANCE.Process32Next(snapshot, pe));
            }
        } finally {
            Kernel32.INSTANCE.CloseHandle(snapshot);
        }
        
        return candidates;
    }
    
    private ProcessCandidate analyzeJavaProcess(long pid, String executable) {
        try {
            String commandLine = getCommandLine(pid);
            if (commandLine == null || commandLine.isEmpty()) {
                return null;
            }
            
            String lowerCmd = commandLine.toLowerCase();
            
            // 第一步：排除非网易启动器
            for (String exclude : EXCLUDE_INDICATORS) {
                if (lowerCmd.contains(exclude.toLowerCase())) {
                    LOGGER.debug("排除进程 {} (包含排除标识: {})", pid, exclude);
                    return null;
                }
            }
            
            // 第二步：必须包含网易标识
            boolean hasNetease = false;
            for (String required : NETEASE_REQUIRED) {
                if (lowerCmd.contains(required)) {
                    hasNetease = true;
                    break;
                }
            }
            
            if (!hasNetease) {
                LOGGER.debug("排除进程 {} (不包含网易标识)", pid);
                return null;
            }
            
            // 第三步：评分（已确认是网易 MC）
            ProcessCandidate.Builder builder = new ProcessCandidate.Builder(pid)
                .executable(executable)
                .commandLine(commandLine);
            
            int score = 50;  // 基础分（已确认网易）
            
            // 网易标识加分
            for (String indicator : NETEASE_INDICATORS) {
                if (lowerCmd.contains(indicator.toLowerCase())) {
                    score += 15;
                    builder.addEvidence("netease_indicator", indicator);
                }
            }
            
            // Minecraft 标识
            if (lowerCmd.contains("minecraft")) {
                score += 20;
                builder.addEvidence("minecraft", "true");
            }
            
            // LWJGL（Minecraft 必需库）
            if (lowerCmd.contains("lwjgl")) {
                score += 15;
                builder.addEvidence("lwjgl", "true");
            }
            
            // 主类
            if (lowerCmd.contains("net.minecraft")) {
                score += 25;
                builder.addEvidence("main_class", "net.minecraft");
            }
            
            // 提取 classpath
            String classpath = extractClasspath(commandLine);
            if (classpath != null && !classpath.isEmpty()) {
                builder.classpath(classpath);
                String lowerClasspath = classpath.toLowerCase();
                if (lowerClasspath.contains("netease")) {
                    score += 10;
                }
                if (lowerClasspath.contains("minecraft")) {
                    score += 10;
                }
            }
            
            // 提取 Java 版本
            String javaVersion = extractJavaVersion(commandLine);
            if (javaVersion != null) {
                builder.javaVersion(javaVersion);
            }
            
            // 提取工作目录
            String workDir = extractWorkingDirectory(commandLine);
            if (workDir != null) {
                builder.workingDirectory(workDir);
                String lowerWorkDir = workDir.toLowerCase();
                if (lowerWorkDir.contains("netease")) {
                    score += 5;
                }
                if (lowerWorkDir.contains("minecraft")) {
                    score += 5;
                }
            }
            
            builder.score(score);
            
            LOGGER.info("检测到网易 Minecraft 进程: PID={}, 得分={}", pid, score);
            
            return builder.build();
            
        } catch (Exception e) {
            LOGGER.debug("分析进程 {} 失败: {}", pid, e.getMessage());
            return null;
        }
    }
    
    private String getCommandLine(long pid) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                "wmic", "process", "where", "ProcessId=" + pid, 
                "get", "CommandLine", "/format:list"
            );
            
            Process process = pb.start();
            String output = new String(process.getInputStream().readAllBytes());
            process.waitFor();
            
            for (String line : output.split("\n")) {
                if (line.startsWith("CommandLine=")) {
                    return line.substring("CommandLine=".length()).trim();
                }
            }
        } catch (Exception e) {
            LOGGER.debug("获取进程 {} 命令行失败", pid);
        }
        return null;
    }
    
    private String extractClasspath(String commandLine) {
        Pattern pattern = Pattern.compile("-cp\\s+\"([^\"]+)\"|-classpath\\s+\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(commandLine);
        if (matcher.find()) {
            return matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
        }
        return null;
    }
    
    private String extractJavaVersion(String commandLine) {
        Pattern pattern = Pattern.compile("java(?:w)?\\.exe|java version \"([^\"]+)\"");
        Matcher matcher = pattern.matcher(commandLine);
        if (matcher.find() && matcher.group(1) != null) {
            return matcher.group(1);
        }
        return "UNKNOWN";
    }
    
    private String extractWorkingDirectory(String commandLine) {
        Pattern pattern = Pattern.compile("-Duser\\.dir=([^\\s]+)");
        Matcher matcher = pattern.matcher(commandLine);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
