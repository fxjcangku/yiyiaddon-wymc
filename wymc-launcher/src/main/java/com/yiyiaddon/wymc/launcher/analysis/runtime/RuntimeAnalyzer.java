package com.yiyiaddon.wymc.launcher.analysis.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RuntimeAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeAnalyzer.class);
    
    public RuntimeAnalysisResult analyze(String workingDirectory, String classpath) {
        LOGGER.info("开始 Runtime 分析");
        
        RuntimeAnalysisResult result = new RuntimeAnalysisResult();
        
        // 检测封装类型
        PackagingType packaging = detectPackaging(workingDirectory, classpath);
        result.setPackagingType(packaging);
        
        // 检测混淆
        ObfuscationType obfuscation = detectObfuscation(classpath);
        result.setObfuscationType(obfuscation);
        
        // 检测自定义 ClassLoader
        boolean customLoader = detectCustomClassLoader(classpath);
        result.setCustomClassLoader(customLoader);
        
        // 检测 Runtime 转换
        boolean runtimeTransform = detectRuntimeTransformation(classpath);
        result.setRuntimeTransformation(runtimeTransform);
        
        LOGGER.info("Runtime 分析完成: packaging={}, obfuscation={}", packaging, obfuscation);
        
        return result;
    }
    
    private PackagingType detectPackaging(String workingDirectory, String classpath) {
        if (classpath == null) return PackagingType.UNKNOWN;
        
        String lower = classpath.toLowerCase();
        
        if (lower.contains("netease") || lower.contains("mc-launcher")) {
            return PackagingType.NETEASE_CUSTOM;
        }
        
        if (lower.contains("fabric-loader") || lower.contains("fabricmc")) {
            return PackagingType.FABRIC;
        }
        
        if (lower.contains("forge") || lower.contains("fml")) {
            return PackagingType.FORGE;
        }
        
        if (lower.contains("minecraft") && !lower.contains("fabric") && !lower.contains("forge")) {
            return PackagingType.VANILLA;
        }
        
        return PackagingType.UNKNOWN;
    }
    
    private ObfuscationType detectObfuscation(String classpath) {
        if (classpath == null) return ObfuscationType.UNKNOWN;
        
        String lower = classpath.toLowerCase();
        
        // 检测 Yarn/Intermediary (Fabric)
        if (lower.contains("intermediary") || lower.contains("yarn")) {
            return ObfuscationType.YARN;
        }
        
        // 检测 MCP/SRG (Forge)
        if (lower.contains("mcp") || lower.contains("srg")) {
            return ObfuscationType.MCP;
        }
        
        // 检测官方混淆
        if (lower.contains("client.jar") || lower.contains("server.jar")) {
            return ObfuscationType.MOJANG_OBFUSCATED;
        }
        
        // 网易可能使用自定义混淆
        if (lower.contains("netease")) {
            return ObfuscationType.CUSTOM_OBFUSCATED;
        }
        
        return ObfuscationType.UNKNOWN;
    }
    
    private boolean detectCustomClassLoader(String classpath) {
        if (classpath == null) return false;
        
        String lower = classpath.toLowerCase();
        return lower.contains("netease") || 
               lower.contains("custom") ||
               lower.contains("launcher");
    }
    
    private boolean detectRuntimeTransformation(String classpath) {
        if (classpath == null) return false;
        
        String lower = classpath.toLowerCase();
        // 检测可能的运行时转换工具
        return lower.contains("asm") || 
               lower.contains("javassist") ||
               lower.contains("bytebuddy") ||
               lower.contains("mixin");
    }
    
    public enum PackagingType {
        VANILLA,
        FABRIC,
        FORGE,
        NETEASE_CUSTOM,
        UNKNOWN
    }
    
    public enum ObfuscationType {
        NONE,
        YARN,
        MCP,
        MOJANG_OBFUSCATED,
        CUSTOM_OBFUSCATED,
        UNKNOWN
    }
    
    public static class RuntimeAnalysisResult {
        private PackagingType packagingType;
        private ObfuscationType obfuscationType;
        private boolean customClassLoader;
        private boolean runtimeTransformation;
        
        public PackagingType getPackagingType() { return packagingType; }
        public void setPackagingType(PackagingType packagingType) { this.packagingType = packagingType; }
        
        public ObfuscationType getObfuscationType() { return obfuscationType; }
        public void setObfuscationType(ObfuscationType obfuscationType) { this.obfuscationType = obfuscationType; }
        
        public boolean hasCustomClassLoader() { return customClassLoader; }
        public void setCustomClassLoader(boolean customClassLoader) { this.customClassLoader = customClassLoader; }
        
        public boolean hasRuntimeTransformation() { return runtimeTransformation; }
        public void setRuntimeTransformation(boolean runtimeTransformation) { this.runtimeTransformation = runtimeTransformation; }
    }
}
