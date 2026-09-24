package com.yiyiaddon.wymc.runtime.fabric;

import com.yiyiaddon.wymc.runtime.bridge.MinecraftBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Minecraft Client Instance 伪装
 * 
 * yiyiaddon 代码中大量使用 Minecraft.getInstance()
 * WYMC 需要提供这个实例
 */
public class MinecraftClientAccessor {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftClientAccessor.class);
    
    private static Object minecraftInstance;
    
    /**
     * 设置 Minecraft 实例
     * 
     * 在 Agent 初始化时，从网易 MC 获取真实的 Minecraft 实例
     */
    public static void setMinecraftInstance(Object instance) {
        minecraftInstance = instance;
        LOGGER.info("Minecraft instance set: {}", instance.getClass().getName());
    }
    
    /**
     * 获取 Minecraft 实例
     * 
     * yiyiaddon 调用 Minecraft.getInstance() 时会被重定向到这里
     */
    public static Object getInstance() {
        if (minecraftInstance == null) {
            // 如果还没设置，尝试从 Bridge 获取
            try {
                MinecraftBridge bridge = MinecraftBridge.getInstance();
                if (bridge.isInitialized()) {
                    Class<?> mcClass = bridge.getClass("Minecraft");
                    if (mcClass != null) {
                        // 通过反射获取单例
                        minecraftInstance = mcClass.getMethod("getInstance").invoke(null);
                        LOGGER.info("Minecraft instance obtained from Bridge");
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Failed to get Minecraft instance", e);
            }
        }
        
        return minecraftInstance;
    }
}
