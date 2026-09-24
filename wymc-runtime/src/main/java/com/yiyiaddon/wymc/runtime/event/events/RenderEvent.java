package com.yiyiaddon.wymc.runtime.event.events;

import com.yiyiaddon.wymc.runtime.event.Event;

/**
 * RenderEvent - 渲染事件
 * 
 * 触发时机：每帧渲染时
 */
public class RenderEvent extends Event {
    
    private final float partialTicks;
    
    public RenderEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return partialTicks;
    }
    
    /**
     * Render2D - 2D 渲染事件（HUD）
     */
    public static class Render2D extends RenderEvent {
        public Render2D(float partialTicks) {
            super(partialTicks);
        }
    }
    
    /**
     * Render3D - 3D 渲染事件（世界）
     */
    public static class Render3D extends RenderEvent {
        public Render3D(float partialTicks) {
            super(partialTicks);
        }
    }
}
