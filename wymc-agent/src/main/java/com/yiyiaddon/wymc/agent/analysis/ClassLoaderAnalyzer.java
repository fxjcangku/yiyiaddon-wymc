package com.yiyiaddon.wymc.agent.analysis;

import java.lang.instrument.Instrumentation;
import java.util.*;

public class ClassLoaderAnalyzer {
    private final Instrumentation instrumentation;
    
    public ClassLoaderAnalyzer(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }
    
    public void analyze() {
        System.out.println("[WYMC] Analyzing ClassLoader structure");
        
        Map<ClassLoader, List<Class<?>>> classLoaderMap = new HashMap<>();
        
        for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
            ClassLoader loader = clazz.getClassLoader();
            classLoaderMap.computeIfAbsent(loader, k -> new ArrayList<>()).add(clazz);
        }
        
        System.out.println("[WYMC] Found " + classLoaderMap.size() + " ClassLoaders");
        
        for (Map.Entry<ClassLoader, List<Class<?>>> entry : classLoaderMap.entrySet()) {
            ClassLoader loader = entry.getKey();
            int classCount = entry.getValue().size();
            
            String loaderName = loader != null ? loader.getClass().getName() : "Bootstrap";
            System.out.println("  - " + loaderName + ": " + classCount + " classes");
            
            // 检查是否包含 Minecraft 类
            boolean hasMinecraft = entry.getValue().stream()
                .anyMatch(c -> c.getName().toLowerCase().contains("minecraft") ||
                              c.getName().toLowerCase().contains("netease"));
            
            if (hasMinecraft) {
                System.out.println("    -> Contains Minecraft/NetEase classes");
            }
        }
    }
}
