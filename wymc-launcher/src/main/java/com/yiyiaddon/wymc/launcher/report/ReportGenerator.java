package com.yiyiaddon.wymc.launcher.report;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ReportGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportGenerator.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    public void generateEnvironmentReport(EnvironmentData data) {
        try {
            Path reportPath = Paths.get(System.getProperty("wymc.reports.dir"), "EnvironmentReport.json");
            
            Map<String, Object> report = new HashMap<>();
            report.put("timestamp", System.currentTimeMillis());
            report.put("pid", data.pid);
            report.put("java", data.javaInfo);
            report.put("minecraft", data.minecraftInfo);
            report.put("runtime", data.runtimeInfo);
            report.put("server", data.serverInfo);
            
            try (FileWriter writer = new FileWriter(reportPath.toFile())) {
                GSON.toJson(report, writer);
            }
            
            LOGGER.info("环境报告已生成: {}", reportPath);
            
        } catch (Exception e) {
            LOGGER.error("生成环境报告失败", e);
        }
    }
    
    public void generateRuntimeUnpackReport(RuntimeUnpackData data) {
        try {
            Path reportPath = Paths.get(System.getProperty("wymc.reports.dir"), "RuntimeUnpackReport.json");
            
            Map<String, Object> report = new HashMap<>();
            report.put("timestamp", System.currentTimeMillis());
            report.put("packaging_detected", data.packagingDetected);
            report.put("obfuscation_detected", data.obfuscationDetected);
            report.put("custom_classloader", data.customClassLoader);
            report.put("runtime_transformation", data.runtimeTransformation);
            report.put("classes_accessible", data.classesAccessible);
            report.put("classes_dumpable", data.classesDumpable);
            report.put("minecraft_classes_identified", data.minecraftClassesIdentified);
            report.put("mapping_established", data.mappingEstablished);
            report.put("notes", data.notes);
            
            try (FileWriter writer = new FileWriter(reportPath.toFile())) {
                GSON.toJson(report, writer);
            }
            
            LOGGER.info("Runtime 脱壳报告已生成: {}", reportPath);
            
        } catch (Exception e) {
            LOGGER.error("生成 Runtime 脱壳报告失败", e);
        }
    }
    
    public void generateRuntimeDifferenceReport(RuntimeDifferenceData data) {
        try {
            Path reportPath = Paths.get(System.getProperty("wymc.reports.dir"), "RuntimeDifferenceReport.json");
            
            Map<String, Object> report = new HashMap<>();
            report.put("timestamp", System.currentTimeMillis());
            report.put("standard_minecraft", data.standardMinecraft);
            report.put("fabric_runtime", data.fabricRuntime);
            report.put("netease_runtime", data.neteaseRuntime);
            report.put("differences", data.differences);
            
            try (FileWriter writer = new FileWriter(reportPath.toFile())) {
                GSON.toJson(report, writer);
            }
            
            LOGGER.info("Runtime 差异报告已生成: {}", reportPath);
            
        } catch (Exception e) {
            LOGGER.error("生成 Runtime 差异报告失败", e);
        }
    }
    
    public static class EnvironmentData {
        public long pid;
        public Map<String, String> javaInfo = new HashMap<>();
        public Map<String, String> minecraftInfo = new HashMap<>();
        public Map<String, Object> runtimeInfo = new HashMap<>();
        public Map<String, String> serverInfo = new HashMap<>();
    }
    
    public static class RuntimeUnpackData {
        public String packagingDetected;
        public String obfuscationDetected;
        public boolean customClassLoader;
        public boolean runtimeTransformation;
        public boolean classesAccessible;
        public boolean classesDumpable;
        public boolean minecraftClassesIdentified;
        public boolean mappingEstablished;
        public List<String> notes = new ArrayList<>();
    }
    
    public static class RuntimeDifferenceData {
        public Map<String, Object> standardMinecraft = new HashMap<>();
        public Map<String, Object> fabricRuntime = new HashMap<>();
        public Map<String, Object> neteaseRuntime = new HashMap<>();
        public List<String> differences = new ArrayList<>();
    }
}
