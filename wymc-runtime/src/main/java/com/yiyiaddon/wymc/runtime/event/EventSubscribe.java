package com.yiyiaddon.wymc.runtime.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EventSubscribe - 事件订阅注解
 * 
 * 用法：
 * <pre>
 * @EventSubscribe
 * public void onTick(TickEvent event) {
 *     // 处理逻辑
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventSubscribe {
    
    /**
     * 优先级
     */
    EventPriority priority() default EventPriority.NORMAL;
}
