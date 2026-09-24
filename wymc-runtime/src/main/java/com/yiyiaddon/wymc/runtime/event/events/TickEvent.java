package com.yiyiaddon.wymc.runtime.event.events;

import com.yiyiaddon.wymc.runtime.event.Event;

/**
 * TickEvent - 游戏 Tick 事件
 * 
 * 触发时机：每个游戏 Tick（20 TPS = 50ms）
 */
public class TickEvent extends Event {
    
    private final long tickCount;
    
    public TickEvent(long tickCount) {
        this.tickCount = tickCount;
    }
    
    public long getTickCount() {
        return tickCount;
    }
}
