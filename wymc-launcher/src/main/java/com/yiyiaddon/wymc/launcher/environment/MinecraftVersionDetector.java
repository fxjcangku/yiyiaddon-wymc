package com.yiyiaddon.wymc.launcher.environment;

import com.yiyiaddon.wymc.launcher.process.ProcessCandidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinecraftVersionDetector {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftVersionDetector.class);
    
    public VersionInfo detectVersion(ProcessCandidate candidate) {
        LOGGER.info("开始识别 Minecraft 版本 (PID: {})", candidate.getPid());
        
        String version = null;
        String source = null;
        VersionConfidence confidence = VersionConfidence.UNKNOWN;
        
        // 从命令行提取版本
        String cmdLine = candidate.getCommandLine();
        if (cmdLine != null && !cmdLine.isEmpty()) {
            version = extractVersionFromCommandLine(cmdLine);
            if (version != null) {
                source = "COMMAND_LINE";
                confidence = VersionConfidence.MEDIUM;
            }
        }
        
        // 从 classpath 提取版本
        if (version == null) {
            String classpath = candidate.getClasspath();
            if (classpath != null && !classpath.isEmpty()) {
                version = extractVersionFromClasspath(classpath);
                if (version != null) {
                    source = "CLASSPATH";
                    confidence = VersionConfidence.MEDIUM;
                }
            }
        }
        
        // 从工作目录提取版本
        if (version == null) {
            String workDir = candidate.getWorkingDirectory();
            if (workDir != null && !workDir.isEmpty()) {
                version = extractVersionFromPath(workDir);
                if (version != null) {
                    source = "WORKING_DIRECTORY";
                    confidence = VersionConfidence.LOW;
                }
            }
        }
        
        if (version == null) {
            version = "UNKNOWN";
            source = "NONE";
            confidence = VersionConfidence.UNKNOWN;
        }
        
        VersionInfo info = new VersionInfo(version, source, confidence);
        LOGGER.info("版本识别结果: {} (来源: {}, 置信度: {})", version, source, confidence);
        
        return info;
    }
    
    private String extractVersionFromCommandLine(String commandLine) {
        // 匹配 --version 参数
        Pattern versionArg = Pattern.compile("--version\\s+([0-9]+\\.[0-9]+(?:\\.[0-9]+)?)");
        Matcher matcher = versionArg.matcher(commandLine);
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        // 匹配 Minecraft 版本号格式
        Pattern versionPattern = Pattern.compile("(?:minecraft|mc)[_-]?([0-9]+\\.[0-9]+(?:\\.[0-9]+)?)", 
            Pattern.CASE_INSENSITIVE);
        matcher = versionPattern.matcher(commandLine);
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        return null;
    }
    
    private String extractVersionFromClasspath(String classpath) {
        Pattern pattern = Pattern.compile("minecraft[_-]([0-9]+\\.[0-9]+(?:\\.[0-9]+)?)", 
            Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(classpath);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    private String extractVersionFromPath(String path) {
        Pattern pattern = Pattern.compile("([0-9]+\\.[0-9]+(?:\\.[0-9]+)?)");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    public static class VersionInfo {
        private final String version;
        private final String source;
        private final VersionConfidence confidence;
        
        public VersionInfo(String version, String source, VersionConfidence confidence) {
            this.version = version;
            this.source = source;
            this.confidence = confidence;
        }
        
        public String getVersion() { return version; }
        public String getSource() { return source; }
        public VersionConfidence getConfidence() { return confidence; }
    }
    
    public enum VersionConfidence {
        HIGH, MEDIUM, LOW, UNKNOWN
    }
}
