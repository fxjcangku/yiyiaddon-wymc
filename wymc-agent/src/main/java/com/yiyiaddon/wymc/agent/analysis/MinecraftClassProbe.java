package com.yiyiaddon.wymc.agent.analysis;

import java.lang.instrument.Instrumentation;
import java.util.*;

public class MinecraftClassProbe {
    private final Instrumentation instrumentation;
    
    private static final String[] MINECRAFT_PATTERNS = {
        "net.minecraft", "com.mojang", "paulscode", "org.lwjgl"
    };
    
    private static final String[] NETEASE_PATTERNS = {
        "netease", "com.netease"
    };
    
    public MinecraftClassProbe(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }
    
    public void probe() {
        System.out.println("[WYMC] Probing Minecraft classes");
        
        Map<String, List<String>> categorizedClasses = new HashMap<>();
        categorizedClasses.put("minecraft", new ArrayList<>());
        categorizedClasses.put("netease", new ArrayList<>());
        categorizedClasses.put("other", new ArrayList<>());
        
        for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
            String className = clazz.getName();
            String lowerName = className.toLowerCase();
            
            if (matchesPatterns(lowerName, MINECRAFT_PATTERNS)) {
                categorizedClasses.get("minecraft").add(className);
            } else if (matchesPatterns(lowerName, NETEASE_PATTERNS)) {
                categorizedClasses.get("netease").add(className);
            }
        }
        
        System.out.println("[WYMC] Minecraft classes: " + categorizedClasses.get("minecraft").size());
        System.out.println("[WYMC] NetEase classes: " + categorizedClasses.get("netease").size());
        
        // 输出部分示例
        List<String> mcClasses = categorizedClasses.get("minecraft");
        if (!mcClasses.isEmpty()) {
            System.out.println("[WYMC] Sample Minecraft classes:");
            for (int i = 0; i < Math.min(5, mcClasses.size()); i++) {
                System.out.println("  - " + mcClasses.get(i));
            }
        }
        
        List<String> neClasses = categorizedClasses.get("netease");
        if (!neClasses.isEmpty()) {
            System.out.println("[WYMC] Sample NetEase classes:");
            for (int i = 0; i < Math.min(5, neClasses.size()); i++) {
                System.out.println("  - " + neClasses.get(i));
            }
        }
    }
    
    private boolean matchesPatterns(String className, String[] patterns) {
        for (String pattern : patterns) {
            if (className.contains(pattern.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
