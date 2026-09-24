package com.yiyiaddon.wymc.runtime.bridge;

/**
 * World Bridge - 世界 API
 */
public class WorldBridge {
    
    private final MinecraftBridge parent;
    
    public WorldBridge(MinecraftBridge parent) {
        this.parent = parent;
    }
    
    /**
     * 获取世界时间
     */
    public long getTime() {
        // TODO: 实现
        return 0;
    }
    
    /**
     * 是否下雨
     */
    public boolean isRaining() {
        // TODO: 实现
        return false;
    }
}
