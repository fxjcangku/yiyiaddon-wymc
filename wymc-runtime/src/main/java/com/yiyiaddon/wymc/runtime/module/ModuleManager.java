package com.yiyiaddon.wymc.runtime.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ModuleManager - 模块管理器
 * 
 * 职责：
 * - 注册/卸载模块
 * - 启用/禁用模块
 * - 模块查询
 * - 模块生命周期管理
 * 
 * 设计：
 * - 单例模式
 * - 线程安全
 */
public class ModuleManager {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleManager.class);
    
    private static ModuleManager instance;
    
    // 所有已注册的模块：模块名 -> 模块实例
    private final Map<String, Module> modules = new ConcurrentHashMap<>();
    
    // 按分类组织的模块
    private final Map<Category, List<Module>> modulesByCategory = new ConcurrentHashMap<>();
    
    private ModuleManager() {
        // 私有构造
    }
    
    /**
     * 获取单例
     */
    public static ModuleManager getInstance() {
        if (instance == null) {
            synchronized (ModuleManager.class) {
                if (instance == null) {
                    instance = new ModuleManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * 注册模块
     * 
     * @param module 模块实例
     */
    public void registerModule(Module module) {
        String name = module.getName();
        
        if (modules.containsKey(name)) {
            LOGGER.warn("Module already registered: {}", name);
            return;
        }
        
        LOGGER.info("Registering module: {} v{} ({})", 
            name, module.getVersion(), module.getCategory().getDisplayName());
        
        // 添加到映射
        modules.put(name, module);
        
        // 添加到分类
        modulesByCategory.computeIfAbsent(module.getCategory(), 
            k -> new ArrayList<>()).add(module);
        
        // 加载模块
        module.load();
        
        // 如果默认启用，则启用
        if (module.getInfo().enabledByDefault()) {
            module.enable();
        }
    }
    
    /**
     * 卸载模块
     * 
     * @param name 模块名称
     */
    public void unregisterModule(String name) {
        Module module = modules.remove(name);
        
        if (module == null) {
            LOGGER.warn("Module not found: {}", name);
            return;
        }
        
        LOGGER.info("Unregistering module: {}", name);
        
        // 从分类移除
        List<Module> categoryList = modulesByCategory.get(module.getCategory());
        if (categoryList != null) {
            categoryList.remove(module);
        }
        
        // 卸载模块
        module.unload();
    }
    
    /**
     * 启用模块
     * 
     * @param name 模块名称
     */
    public void enableModule(String name) {
        Module module = modules.get(name);
        
        if (module == null) {
            LOGGER.warn("Module not found: {}", name);
            return;
        }
        
        module.enable();
    }
    
    /**
     * 禁用模块
     * 
     * @param name 模块名称
     */
    public void disableModule(String name) {
        Module module = modules.get(name);
        
        if (module == null) {
            LOGGER.warn("Module not found: {}", name);
            return;
        }
        
        module.disable();
    }
    
    /**
     * 切换模块状态
     * 
     * @param name 模块名称
     */
    public void toggleModule(String name) {
        Module module = modules.get(name);
        
        if (module == null) {
            LOGGER.warn("Module not found: {}", name);
            return;
        }
        
        if (module.isEnabled()) {
            module.disable();
        } else {
            module.enable();
        }
    }
    
    /**
     * 获取模块
     * 
     * @param name 模块名称
     * @return 模块实例，不存在返回 null
     */
    public Module getModule(String name) {
        return modules.get(name);
    }
    
    /**
     * 获取模块（类型安全）
     * 
     * @param clazz 模块类
     * @return 模块实例，不存在返回 null
     */
    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) modules.values().stream()
            .filter(m -> m.getClass() == clazz)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * 获取所有模块
     * 
     * @return 不可修改的模块列表
     */
    public Collection<Module> getAllModules() {
        return Collections.unmodifiableCollection(modules.values());
    }
    
    /**
     * 获取指定分类的模块
     * 
     * @param category 分类
     * @return 不可修改的模块列表
     */
    public List<Module> getModulesByCategory(Category category) {
        List<Module> list = modulesByCategory.get(category);
        return list != null ? Collections.unmodifiableList(list) : Collections.emptyList();
    }
    
    /**
     * 获取已启用的模块
     * 
     * @return 已启用的模块列表
     */
    public List<Module> getEnabledModules() {
        return modules.values().stream()
            .filter(Module::isEnabled)
            .collect(Collectors.toList());
    }
    
    /**
     * 启用所有模块
     */
    public void enableAll() {
        modules.values().forEach(Module::enable);
        LOGGER.info("Enabled all modules");
    }
    
    /**
     * 禁用所有模块
     */
    public void disableAll() {
        modules.values().forEach(Module::disable);
        LOGGER.info("Disabled all modules");
    }
    
    /**
     * 卸载所有模块
     */
    public void unregisterAll() {
        new ArrayList<>(modules.keySet()).forEach(this::unregisterModule);
        LOGGER.info("Unregistered all modules");
    }
    
    /**
     * 获取统计信息
     */
    public String getStats() {
        long enabled = modules.values().stream().filter(Module::isEnabled).count();
        return String.format("Modules: %d total, %d enabled", modules.size(), enabled);
    }
}
