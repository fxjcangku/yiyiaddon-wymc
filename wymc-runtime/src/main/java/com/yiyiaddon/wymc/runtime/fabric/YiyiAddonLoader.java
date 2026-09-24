package com.yiyiaddon.wymc.runtime.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * yiyiaddon JAR 加载器
 * 
 * 功能：
 * 1. 加载 yiyiaddon.jar
 * 2. 找到 ClientModInitializer 实现类
 * 3. 调用 onInitializeClient()
 * 4. 注册到 Fabric Loader
 */
public class YiyiAddonLoader {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(YiyiAddonLoader.class);
    
    /**
     * 加载 yiyiaddon
     * 
     * @param jarPath yiyiaddon.jar 路径
     */
    public static void load(String jarPath) throws Exception {
        LOGGER.info("Loading yiyiaddon from: {}", jarPath);
        
        File jarFile = new File(jarPath);
        if (!jarFile.exists()) {
            throw new IllegalArgumentException("JAR not found: " + jarPath);
        }
        
        // 1. 创建 ClassLoader
        URL jarUrl = jarFile.toURI().toURL();
        URLClassLoader classLoader = new URLClassLoader(
            new URL[]{jarUrl},
            YiyiAddonLoader.class.getClassLoader()
        );
        
        // 2. 查找 ClientModInitializer 实现类
        String initializerClass = findClientModInitializer(jarFile);
        if (initializerClass == null) {
            throw new IllegalStateException("ClientModInitializer not found in JAR");
        }
        
        LOGGER.info("Found ClientModInitializer: {}", initializerClass);
        
        // 3. 加载并实例化
        Class<?> clazz = classLoader.loadClass(initializerClass);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        
        // 4. 调用 onInitializeClient()
        clazz.getMethod("onInitializeClient").invoke(instance);
        
        // 5. 注册到 Fabric Loader
        FakeModMetadata metadata = new FakeModMetadata(
            "yiyiaddon",
            "1.0.0",
            "YiYi Addon",
            "Minecraft 辅助工具"
        );
        FakeModContainer container = new FakeModContainer(metadata, instance);
        FakeFabricLoader.getInstance().loadMod(container);
        
        LOGGER.info("yiyiaddon loaded successfully");
    }
    
    /**
     * 查找 ClientModInitializer 实现类
     */
    private static String findClientModInitializer(File jarFile) throws Exception {
        try (JarFile jar = new JarFile(jarFile)) {
            // 方法 1: 查找 fabric.mod.json
            JarEntry modJson = jar.getJarEntry("fabric.mod.json");
            if (modJson != null) {
                // TODO: 解析 fabric.mod.json 中的 entrypoints.client
                // 暂时硬编码
                return "com.yiyiaddon.YiyiAddonClient";
            }
            
            // 方法 2: 扫描所有类（性能差，作为 fallback）
            // TODO: 实现类扫描
            
            // 方法 3: 已知类名（最快）
            return "com.yiyiaddon.YiyiAddonClient";
        }
    }
}
