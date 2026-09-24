package com.yiyiaddon.wymc.runtime.module;

import com.yiyiaddon.wymc.runtime.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Module - 功能模块基类
 * 
 * 所有功能模块都继承此类
 * 
 * 生命周期：
 * 1. 构造 -> 2. onLoad -> 3. onEnable -> 4. onDisable -> 5. onUnload
 */
public abstract class Module {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    // 模块元信息
    private final ModuleInfo info;
    
    // 模块状态
    private boolean loaded = false;
    private boolean enabled = false;
    
    public Module() {
        // 从注解获取元信息
        if (!getClass().isAnnotationPresent(ModuleInfo.class)) {
            throw new IllegalStateException("Module must have @ModuleInfo annotation");
        }
        this.info = getClass().getAnnotation(ModuleInfo.class);
    }
    
    // ========== 生命周期方法 ==========
    
    /**
     * 模块加载
     * 调用时机：模块被 ModuleManager 加载时
     * 用途：初始化资源、配置
     */
    public void onLoad() {
        // 子类可选实现
    }
    
    /**
     * 模块启用
     * 调用时机：模块被启用时
     * 用途：注册事件监听器、开始功能
     */
    public void onEnable() {
        // 子类可选实现
    }
    
    /**
     * 模块禁用
     * 调用时机：模块被禁用时
     * 用途：取消注册、停止功能
     */
    public void onDisable() {
        // 子类可选实现
    }
    
    /**
     * 模块卸载
     * 调用时机：模块被卸载时
     * 用途：清理资源
     */
    public void onUnload() {
        // 子类可选实现
    }
    
    // ========== 内部方法（由 ModuleManager 调用）==========
    
    final void load() {
        if (loaded) {
            logger.warn("Module already loaded: {}", getName());
            return;
        }
        
        logger.info("Loading module: {}", getName());
        onLoad();
        loaded = true;
    }
    
    final void unload() {
        if (!loaded) {
            logger.warn("Module not loaded: {}", getName());
            return;
        }
        
        if (enabled) {
            disable();
        }
        
        logger.info("Unloading module: {}", getName());
        onUnload();
        loaded = false;
    }
    
    final void enable() {
        if (!loaded) {
            throw new IllegalStateException("Module must be loaded before enable");
        }
        
        if (enabled) {
            logger.warn("Module already enabled: {}", getName());
            return;
        }
        
        logger.info("Enabling module: {}", getName());
        
        // 注册事件监听器
        EventBus.getInstance().register(this);
        
        onEnable();
        enabled = true;
    }
    
    final void disable() {
        if (!enabled) {
            logger.warn("Module not enabled: {}", getName());
            return;
        }
        
        logger.info("Disabling module: {}", getName());
        
        onDisable();
        
        // 取消注册事件监听器
        EventBus.getInstance().unregister(this);
        
        enabled = false;
    }
    
    // ========== Getter ==========
    
    public String getName() {
        return info.name();
    }
    
    public String getDescription() {
        return info.description();
    }
    
    public String getVersion() {
        return info.version();
    }
    
    public Category getCategory() {
        return info.category();
    }
    
    public boolean isLoaded() {
        return loaded;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public ModuleInfo getInfo() {
        return info;
    }
}
