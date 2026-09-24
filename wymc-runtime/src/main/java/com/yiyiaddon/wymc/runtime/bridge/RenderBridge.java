package com.yiyiaddon.wymc.runtime.bridge;

/**
 * Render Bridge - 渲染 API
 */
public class RenderBridge {
    
    private final MinecraftBridge parent;
    
    public RenderBridge(MinecraftBridge parent) {
        this.parent = parent;
    }
    
    /**
     * 获取窗口宽度
     */
    public int getWindowWidth() {
        // TODO: 实现
        return 1920;
    }
    
    /**
     * 获取窗口高度
     */
    public int getWindowHeight() {
        // TODO: 实现
        return 1080;
    }
}
