package com.yiyiaddon.wymc.runtime.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * EventBus - 事件总线
 * 
 * 功能：
 * - 注册事件监听器
 * - 发布事件
 * - 支持优先级
 * - 支持异步事件
 * 
 * 设计：
 * - 线程安全
 * - 高性能
 * - 类型安全
 */
public class EventBus {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class);
    
    private static EventBus instance;
    
    // 事件监听器映射：事件类 -> 监听器列表
    private final Map<Class<? extends Event>, List<EventHandler>> handlers = new ConcurrentHashMap<>();
    
    private EventBus() {
        // 私有构造
    }
    
    /**
     * 获取单例
     */
    public static EventBus getInstance() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }
    
    /**
     * 注册监听器对象
     * 
     * @param listener 监听器对象
     */
    public void register(Object listener) {
        Class<?> clazz = listener.getClass();
        
        // 查找所有 @EventSubscribe 标注的方法
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventSubscribe.class)) {
                registerMethod(listener, method);
            }
        }
        
        LOGGER.debug("Registered listener: {}", clazz.getSimpleName());
    }
    
    /**
     * 注册单个方法
     */
    private void registerMethod(Object listener, Method method) {
        // 检查方法签名
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length != 1) {
            LOGGER.warn("Invalid event handler method: {} (must have exactly one parameter)", method.getName());
            return;
        }
        
        Class<?> eventType = paramTypes[0];
        if (!Event.class.isAssignableFrom(eventType)) {
            LOGGER.warn("Invalid event handler method: {} (parameter must extend Event)", method.getName());
            return;
        }
        
        @SuppressWarnings("unchecked")
        Class<? extends Event> eventClass = (Class<? extends Event>) eventType;
        
        // 获取优先级
        EventSubscribe annotation = method.getAnnotation(EventSubscribe.class);
        EventPriority priority = annotation.priority();
        
        // 创建 Handler 包装
        method.setAccessible(true);
        EventHandlerWrapper wrapper = new EventHandlerWrapper(listener, method, priority);
        
        // 添加到映射
        handlers.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>()).add(wrapper);
        
        // 按优先级排序
        List<EventHandler> list = handlers.get(eventClass);
        list.sort(Comparator.comparingInt(h -> ((EventHandlerWrapper) h).priority.ordinal()));
        
        LOGGER.trace("Registered handler for event: {} (priority: {})", eventClass.getSimpleName(), priority);
    }
    
    /**
     * 取消注册监听器
     * 
     * @param listener 监听器对象
     */
    public void unregister(Object listener) {
        for (List<EventHandler> list : handlers.values()) {
            list.removeIf(h -> {
                if (h instanceof EventHandlerWrapper) {
                    return ((EventHandlerWrapper) h).listener == listener;
                }
                return false;
            });
        }
        
        LOGGER.debug("Unregistered listener: {}", listener.getClass().getSimpleName());
    }
    
    /**
     * 发布事件
     * 
     * @param event 事件对象
     */
    public void post(Event event) {
        Class<? extends Event> eventClass = event.getClass();
        List<EventHandler> list = handlers.get(eventClass);
        
        if (list == null || list.isEmpty()) {
            return;
        }
        
        LOGGER.trace("Posting event: {}", eventClass.getSimpleName());
        
        for (EventHandler handler : list) {
            try {
                handler.handle(event);
                
                // 如果事件被取消，停止传播
                if (event.isCancelled()) {
                    LOGGER.trace("Event cancelled: {}", eventClass.getSimpleName());
                    break;
                }
            } catch (Exception e) {
                LOGGER.error("Error handling event: {}", eventClass.getSimpleName(), e);
            }
        }
    }
    
    /**
     * 异步发布事件
     * 
     * @param event 事件对象
     */
    public void postAsync(Event event) {
        new Thread(() -> post(event), "EventBus-Async").start();
    }
    
    /**
     * 清空所有监听器
     */
    public void clear() {
        handlers.clear();
        LOGGER.info("EventBus cleared");
    }
    
    /**
     * 获取统计信息
     */
    public String getStats() {
        int totalHandlers = handlers.values().stream()
            .mapToInt(List::size)
            .sum();
        return String.format("Events: %d, Handlers: %d", handlers.size(), totalHandlers);
    }
    
    // ========== 内部接口 ==========
    
    /**
     * 事件处理器接口
     */
    private interface EventHandler {
        void handle(Event event) throws Exception;
    }
    
    /**
     * 事件处理器包装
     */
    private static class EventHandlerWrapper implements EventHandler {
        final Object listener;
        final Method method;
        final EventPriority priority;
        
        EventHandlerWrapper(Object listener, Method method, EventPriority priority) {
            this.listener = listener;
            this.method = method;
            this.priority = priority;
        }
        
        @Override
        public void handle(Event event) throws Exception {
            method.invoke(listener, event);
        }
    }
}
