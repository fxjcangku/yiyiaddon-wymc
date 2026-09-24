package com.yiyiaddon.wymc.runtime.event;

/**
 * Event - 事件基类
 * 
 * 所有事件都继承此类
 */
public abstract class Event {
    
    // 是否已取消
    private boolean cancelled = false;
    
    // 事件时间戳
    private final long timestamp = System.currentTimeMillis();
    
    /**
     * 是否可取消
     */
    public boolean isCancellable() {
        return false;
    }
    
    /**
     * 是否已取消
     */
    public boolean isCancelled() {
        return cancelled;
    }
    
    /**
     * 设置取消状态
     */
    public void setCancelled(boolean cancelled) {
        if (!isCancellable()) {
            throw new UnsupportedOperationException("Event is not cancellable");
        }
        this.cancelled = cancelled;
    }
    
    /**
     * 获取事件时间戳
     */
    public long getTimestamp() {
        return timestamp;
    }
}
