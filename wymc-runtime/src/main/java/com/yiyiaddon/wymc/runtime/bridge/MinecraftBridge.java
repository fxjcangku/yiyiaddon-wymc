package com.yiyiaddon.wymc.runtime.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Minecraft Bridge - 游戏 API 访问入口
 * 
 * 职责：
 * - 初始化游戏对象访问
 * - 处理混淆/非混淆差异
 * - 提供统一 API 接口
 * - 缓存反射结果
 * 
 * 设计：
 * - 单例模式
 * - 延迟初始化
 * - 自动检测混淆
 * - 特征匹配
 */
public class MinecraftBridge {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftBridge.class);
    
    private static MinecraftBridge instance;
    
    // 是否已初始化
    private boolean initialized = false;
    
    // 混淆类型
    private ObfuscationType obfuscationType = ObfuscationType.UNKNOWN;
    
    // Mapping 缓存
    private final Map<String, Class<?>> classMapping = new HashMap<>();
    private final Map<String, Method> methodMapping = new HashMap<>();
    
    // 子 Bridge
    private GameBridge gameBridge;
    private PlayerBridge playerBridge;
    private WorldBridge worldBridge;
    private RenderBridge renderBridge;
    
    private MinecraftBridge() {
        // 私有构造
    }
    
    /**
     * 获取单例
     */
    public static MinecraftBridge getInstance() {
        if (instance == null) {
            synchronized (MinecraftBridge.class) {
                if (instance == null) {
                    instance = new MinecraftBridge();
                }
            }
        }
        return instance;
    }
    
    /**
     * 初始化 Bridge
     * 
     * @throws BridgeException 初始化失败
     */
    public void initialize() throws BridgeException {
        if (initialized) {
            LOGGER.warn("MinecraftBridge already initialized");
            return;
        }
        
        LOGGER.info("Initializing MinecraftBridge...");
        
        try {
            // 1. 检测混淆类型
            detectObfuscation();
            
            // 2. 建立核心类 Mapping
            buildCoreMapping();
            
            // 3. 初始化子 Bridge
            initializeSubBridges();
            
            initialized = true;
            LOGGER.info("MinecraftBridge initialized successfully (Obfuscation: {})", obfuscationType);
            
        } catch (Exception e) {
            LOGGER.error("Failed to initialize MinecraftBridge", e);
            throw new BridgeException("Bridge initialization failed", e);
        }
    }
    
    /**
     * 检测混淆类型
     */
    private void detectObfuscation() {
        LOGGER.debug("Detecting obfuscation type...");
        
        // 尝试 1: 未混淆（标准类名）
        if (tryLoadClass("net.minecraft.client.Minecraft") != null) {
            obfuscationType = ObfuscationType.NONE;
            LOGGER.info("Detected: No obfuscation (standard names)");
            return;
        }
        
        // 尝试 2: Yarn Mapping
        if (tryLoadClass("net.minecraft.class_310") != null) {
            obfuscationType = ObfuscationType.YARN;
            LOGGER.info("Detected: Yarn mapping");
            return;
        }
        
        // 尝试 3: Mojang 混淆（通过特征匹配）
        Class<?> minecraftClass = findMinecraftClassBySignature();
        if (minecraftClass != null) {
            String className = minecraftClass.getName();
            if (className.matches("^[a-z]{1,3}$")) {
                obfuscationType = ObfuscationType.MOJANG;
                LOGGER.info("Detected: Mojang obfuscation (class: {})", className);
                classMapping.put("Minecraft", minecraftClass);
                return;
            }
        }
        
        // 尝试 4: 网易自定义
        if (tryLoadClass("com.netease.mc.Client") != null) {
            obfuscationType = ObfuscationType.NETEASE_CUSTOM;
            LOGGER.info("Detected: NetEase custom obfuscation");
            return;
        }
        
        // 未知
        obfuscationType = ObfuscationType.UNKNOWN;
        LOGGER.warn("Could not detect obfuscation type");
    }
    
    /**
     * 建立核心类 Mapping
     */
    private void buildCoreMapping() throws BridgeException {
        LOGGER.debug("Building core class mapping...");
        
        switch (obfuscationType) {
            case NONE:
                buildStandardMapping();
                break;
            case YARN:
                buildYarnMapping();
                break;
            case MOJANG:
                buildMojangMapping();
                break;
            case NETEASE_CUSTOM:
                buildNeteaseMapping();
                break;
            default:
                throw new BridgeException("Unknown obfuscation type, cannot build mapping");
        }
        
        LOGGER.info("Core mapping built: {} classes", classMapping.size());
    }
    
    /**
     * 标准类名 Mapping
     */
    private void buildStandardMapping() {
        classMapping.put("Minecraft", loadClass("net.minecraft.client.Minecraft"));
        classMapping.put("LocalPlayer", loadClass("net.minecraft.client.player.LocalPlayer"));
        classMapping.put("ClientLevel", loadClass("net.minecraft.client.multiplayer.ClientLevel"));
        classMapping.put("Window", loadClass("com.mojang.blaze3d.platform.Window"));
        // TODO: 添加更多核心类
    }
    
    /**
     * Yarn Mapping
     */
    private void buildYarnMapping() {
        classMapping.put("Minecraft", loadClass("net.minecraft.class_310"));
        classMapping.put("LocalPlayer", loadClass("net.minecraft.class_746"));
        classMapping.put("ClientLevel", loadClass("net.minecraft.class_638"));
        // TODO: 添加更多 Yarn 映射
    }
    
    /**
     * Mojang 混淆 Mapping（通过特征匹配）
     */
    private void buildMojangMapping() throws BridgeException {
        // Minecraft 类已在检测时找到
        if (!classMapping.containsKey("Minecraft")) {
            throw new BridgeException("Minecraft class not found");
        }
        
        // TODO: 通过特征匹配找到其他核心类
        // - LocalPlayer: 继承自 Player，持有 ClientLevel
        // - ClientLevel: 持有大量 Entity
        // - Window: 持有 GLFW window handle
    }
    
    /**
     * 网易自定义 Mapping
     */
    private void buildNeteaseMapping() {
        // TODO: 根据实测结果实现
        classMapping.put("Minecraft", loadClass("com.netease.mc.Client"));
    }
    
    /**
     * 初始化子 Bridge
     */
    private void initializeSubBridges() {
        gameBridge = new GameBridge(this);
        playerBridge = new PlayerBridge(this);
        worldBridge = new WorldBridge(this);
        renderBridge = new RenderBridge(this);
    }
    
    /**
     * 通过特征匹配查找 Minecraft 主类
     * 
     * 特征：
     * - 单例模式（static instance 字段）
     * - 持有 Window 对象
     * - 持有 Player/World 对象
     */
    private Class<?> findMinecraftClassBySignature() {
        LOGGER.debug("Searching Minecraft class by signature...");
        
        // 获取所有已加载的类
        Class<?>[] allClasses;
        try {
            // 从 Instrumentation 获取（需要 Agent 支持）
            // 这里简化处理，实际需要通过 Agent 传递
            return null; // TODO: 实现特征匹配
        } catch (Exception e) {
            LOGGER.error("Failed to search by signature", e);
            return null;
        }
    }
    
    /**
     * 尝试加载类
     */
    private Class<?> tryLoadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    /**
     * 加载类（必须存在）
     */
    private Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + className, e);
        }
    }
    
    // ========== Getter ==========
    
    public boolean isInitialized() {
        return initialized;
    }
    
    public ObfuscationType getObfuscationType() {
        return obfuscationType;
    }
    
    public GameBridge game() {
        checkInitialized();
        return gameBridge;
    }
    
    public PlayerBridge player() {
        checkInitialized();
        return playerBridge;
    }
    
    public WorldBridge world() {
        checkInitialized();
        return worldBridge;
    }
    
    public RenderBridge render() {
        checkInitialized();
        return renderBridge;
    }
    
    /**
     * 获取映射的类
     */
    public Class<?> getClass(String name) {
        return classMapping.get(name);
    }
    
    /**
     * 获取映射的方法
     */
    public Method getMethod(String name) {
        return methodMapping.get(name);
    }
    
    private void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException("MinecraftBridge not initialized");
        }
    }
    
    // ========== 内部类 ==========
    
    /**
     * 混淆类型
     */
    public enum ObfuscationType {
        NONE,            // 未混淆
        YARN,            // Yarn Mapping
        MOJANG,          // Mojang 混淆
        NETEASE_CUSTOM,  // 网易自定义
        UNKNOWN          // 未知
    }
    
    /**
     * Bridge 异常
     */
    public static class BridgeException extends Exception {
        public BridgeException(String message) {
            super(message);
        }
        
        public BridgeException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
