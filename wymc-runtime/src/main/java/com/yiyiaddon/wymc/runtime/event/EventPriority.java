package com.yiyiaddon.wymc.runtime.event;

/**
 * EventPriority - 事件优先级
 * 
 * 优先级从高到低：
 * HIGHEST -> HIGH -> NORMAL -> LOW -> LOWEST
 */
public enum EventPriority {
    
    /**
     * 最高优先级
     * 用于：关键功能、安全检查
     */
    HIGHEST,
    
    /**
     * 高优先级
     * 用于：核心功能
     */
    HIGH,
    
    /**
     * 普通优先级（默认）
     * 用于：一般功能
     */
    NORMAL,
    
    /**
     * 低优先级
     * 用于：辅助功能
     */
    LOW,
    
    /**
     * 最低优先级
     * 用于：日志、统计
     */
    LOWEST
}
