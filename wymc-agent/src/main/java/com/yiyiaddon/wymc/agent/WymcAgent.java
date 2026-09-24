package com.yiyiaddon.wymc.agent;

import com.yiyiaddon.wymc.agent.analysis.ClassLoaderAnalyzer;
import com.yiyiaddon.wymc.agent.analysis.MinecraftClassProbe;

import java.lang.instrument.Instrumentation;

/**
 * WYMC Agent 入口
 * 
 * Phase 1: Runtime 分析
 * Phase 2: 加载 yiyiaddon
 * 
 * 注入时机：游戏启动后
 */
public class WymcAgent {
    
    private static Instrumentation instrumentation;
    private static RuntimeContext runtimeContext;
    private static boolean initialized = false;
    
    /**
     * Agent 入口（premain）
     * 使用场景：-javaagent 启动参数
     */
    public static void premain(String args, Instrumentation inst) {
        System.out.println("[WYMC] Agent premain called");
        agentmain(args, inst);
    }
    
    /**
     * Agent 入口（agentmain）
     * 使用场景：Attach API 动态注入
     */
    public static void agentmain(String args, Instrumentation inst) {
        if (initialized) {
            System.out.println("[WYMC] Agent already initialized");
            return;
        }
        
        System.out.println("[WYMC] ====================================");
        System.out.println("[WYMC] YiYi WYMC Agent Starting...");
        System.out.println("[WYMC] ====================================");
        
        instrumentation = inst;
        
        try {
            // Phase 1: 初始化 Runtime 上下文
            initializeRuntime();
            
            // Phase 2: 加载 yiyiaddon（如果指定）
            if (args != null && args.contains("load-yiyiaddon")) {
                loadYiyiAddon(args);
            }
            
            initialized = true;
            System.out.println("[WYMC] Agent initialized successfully");
            
        } catch (Exception e) {
            System.err.println("[WYMC] Agent initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 初始化 Runtime
     */
    private static void initializeRuntime() {
        System.out.println("[WYMC] Initializing Runtime Context...");
        
        runtimeContext = new RuntimeContext(instrumentation);
        
        // 分析 ClassLoader
        ClassLoaderAnalyzer classLoaderAnalyzer = new ClassLoaderAnalyzer(instrumentation);
        classLoaderAnalyzer.analyzeClassLoaders();
        
        // 探测 Minecraft 类
        MinecraftClassProbe classProbe = new MinecraftClassProbe(instrumentation);
        classProbe.probeMinecraftClasses();
        
        System.out.println("[WYMC] Runtime Context initialized");
        System.out.println("[WYMC] Found " + instrumentation.getAllLoadedClasses().length + " loaded classes");
    }
    
    /**
     * 加载 yiyiaddon
     * 
     * @param args Agent 参数，格式：load-yiyiaddon=/path/to/yiyiaddon.jar
     */
    private static void loadYiyiAddon(String args) {
        try {
            System.out.println("[WYMC] ====================================");
            System.out.println("[WYMC] Loading yiyiaddon...");
            System.out.println("[WYMC] ====================================");
            
            // 解析 JAR 路径
            String jarPath = extractJarPath(args);
            if (jarPath == null) {
                System.err.println("[WYMC] Invalid args, JAR path not found");
                return;
            }
            
            System.out.println("[WYMC] yiyiaddon JAR: " + jarPath);
            
            // 初始化 Fabric 兼容层
            System.out.println("[WYMC] Initializing Fabric compatibility layer...");
            initializeFabricCompat();
            
            // 加载 yiyiaddon.jar
            System.out.println("[WYMC] Loading yiyiaddon.jar...");
            Class<?> loaderClass = Class.forName("com.yiyiaddon.wymc.runtime.fabric.YiyiAddonLoader");
            loaderClass.getMethod("load", String.class).invoke(null, jarPath);
            
            System.out.println("[WYMC] ====================================");
            System.out.println("[WYMC] yiyiaddon loaded successfully!");
            System.out.println("[WYMC] ====================================");
            
        } catch (Exception e) {
            System.err.println("[WYMC] Failed to load yiyiaddon: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 初始化 Fabric 兼容层
     */
    private static void initializeFabricCompat() throws Exception {
        // 初始化 Fake Fabric Loader
        Class<?> fabricLoaderClass = Class.forName("com.yiyiaddon.wymc.runtime.fabric.FakeFabricLoader");
        fabricLoaderClass.getMethod("getInstance").invoke(null);
        
        // 初始化 Fabric Events 兼容
        Class<?> fabricEventsClass = Class.forName("com.yiyiaddon.wymc.runtime.fabric.FabricEventsCompat");
        fabricEventsClass.getMethod("getInstance").invoke(null);
        
        System.out.println("[WYMC] Fabric compatibility layer initialized");
    }
    
    /**
     * 从参数中提取 JAR 路径
     * 
     * 格式：load-yiyiaddon=/path/to/yiyiaddon.jar
     */
    private static String extractJarPath(String args) {
        if (args == null || !args.contains("=")) {
            return null;
        }
        
        String[] parts = args.split("=", 2);
        if (parts.length != 2) {
            return null;
        }
        
        return parts[1].trim();
    }
    
    /**
     * 获取 Instrumentation 实例
     */
    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
    
    /**
     * 获取 Runtime 上下文
     */
    public static RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }
}
