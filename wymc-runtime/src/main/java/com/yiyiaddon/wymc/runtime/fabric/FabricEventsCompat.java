package com.yiyiaddon.wymc.runtime.fabric;

import com.yiyiaddon.wymc.runtime.event.EventBus;
import com.yiyiaddon.wymc.runtime.event.events.TickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Fabric Events 兼容层
 * 
 * 将 Fabric 事件转换为 WYMC EventBus 事件
 * 
 * 支持的事件：
 * - ClientTickEvents.END_CLIENT_TICK
 * - ClientTickEvents.START_CLIENT_TICK
 * - (按需添加更多)
 */
public class FabricEventsCompat {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FabricEventsCompat.class);
    
    private static FabricEventsCompat instance;
    
    // ClientTickEvents 监听器
    private final List<Consumer<Object>> endClientTickListeners = new ArrayList<>();
    private final List<Consumer<Object>> startClientTickListeners = new ArrayList<>();
    
    private FabricEventsCompat() {
        // 注册到 WYMC EventBus
        EventBus.getInstance().register(this);
        LOGGER.info("Fabric Events compatibility layer initialized");
    }
    
    public static FabricEventsCompat getInstance() {
        if (instance == null) {
            synchronized (FabricEventsCompat.class) {
                if (instance == null) {
                    instance = new FabricEventsCompat();
                }
            }
        }
        return instance;
    }
    
    /**
     * 注册 END_CLIENT_TICK 监听器
     * 
     * 对应 Fabric API:
     * ClientTickEvents.END_CLIENT_TICK.register(client -> { ... });
     */
    public void registerEndClientTick(Consumer<Object> listener) {
        endClientTickListeners.add(listener);
        LOGGER.trace("Registered END_CLIENT_TICK listener");
    }
    
    /**
     * 注册 START_CLIENT_TICK 监听器
     */
    public void registerStartClientTick(Consumer<Object> listener) {
        startClientTickListeners.add(listener);
        LOGGER.trace("Registered START_CLIENT_TICK listener");
    }
    
    /**
     * 处理 WYMC TickEvent
     * 
     * 转换为 Fabric ClientTickEvents
     */
    @com.yiyiaddon.wymc.runtime.event.EventSubscribe
    public void onTick(TickEvent event) {
        // START_CLIENT_TICK
        for (Consumer<Object> listener : startClientTickListeners) {
            try {
                listener.accept(null); // Minecraft client instance (TODO: 从 Bridge 获取)
            } catch (Exception e) {
                LOGGER.error("Error in START_CLIENT_TICK listener", e);
            }
        }
        
        // END_CLIENT_TICK
        for (Consumer<Object> listener : endClientTickListeners) {
            try {
                listener.accept(null); // Minecraft client instance
            } catch (Exception e) {
                LOGGER.error("Error in END_CLIENT_TICK listener", e);
            }
        }
    }
    
    /**
     * 模拟 Fabric ClientTickEvents 类
     * 
     * 提供给 yiyiaddon 使用：
     * ClientTickEvents.END_CLIENT_TICK.register(...)
     */
    public static class ClientTickEvents {
        
        public static final FakeEvent<Consumer<Object>> END_CLIENT_TICK = new FakeEvent<>() {
            @Override
            public void register(Consumer<Object> listener) {
                FabricEventsCompat.getInstance().registerEndClientTick(listener);
            }
        };
        
        public static final FakeEvent<Consumer<Object>> START_CLIENT_TICK = new FakeEvent<>() {
            @Override
            public void register(Consumer<Object> listener) {
                FabricEventsCompat.getInstance().registerStartClientTick(listener);
            }
        };
    }
    
    /**
     * 假的 Fabric Event 接口
     */
    public interface FakeEvent<T> {
        void register(T listener);
    }
}
