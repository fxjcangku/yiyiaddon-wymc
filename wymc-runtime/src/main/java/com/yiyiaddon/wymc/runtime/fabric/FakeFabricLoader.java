package com.yiyiaddon.wymc.runtime.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

/**
 * Fabric Loader 伪装实现
 * 
 * 让 yiyiaddon 以为运行在 Fabric 环境中
 * 
 * 伪装内容：
 * - FabricLoader.getInstance()
 * - getGameDir()
 * - getConfigDir()
 * - getMods()
 */
public class FakeFabricLoader {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FakeFabricLoader.class);
    
    private static FakeFabricLoader instance;
    
    private final Path gameDir;
    private final Path configDir;
    private final List<FakeModContainer> mods = new ArrayList<>();
    
    private FakeFabricLoader() {
        // 使用 .minecraft 目录（或网易 MC 的对应目录）
        String userHome = System.getProperty("user.home");
        this.gameDir = new File(userHome, ".minecraft").toPath();
        this.configDir = gameDir.resolve("config");
        
        LOGGER.info("Fake Fabric Loader initialized");
        LOGGER.info("Game dir: {}", gameDir);
        LOGGER.info("Config dir: {}", configDir);
    }
    
    public static FakeFabricLoader getInstance() {
        if (instance == null) {
            synchronized (FakeFabricLoader.class) {
                if (instance == null) {
                    instance = new FakeFabricLoader();
                }
            }
        }
        return instance;
    }
    
    /**
     * 加载 Mod
     */
    public void loadMod(FakeModContainer mod) {
        mods.add(mod);
        LOGGER.info("Loaded mod: {} v{}", mod.getMetadata().getId(), 
            mod.getMetadata().getVersion());
    }
    
    /**
     * 获取游戏目录
     */
    public Path getGameDir() {
        return gameDir;
    }
    
    /**
     * 获取配置目录
     */
    public Path getConfigDir() {
        return configDir;
    }
    
    /**
     * 获取所有 Mod
     */
    public Collection<FakeModContainer> getAllMods() {
        return Collections.unmodifiableList(mods);
    }
    
    /**
     * 获取指定 Mod
     */
    public Optional<FakeModContainer> getModContainer(String id) {
        return mods.stream()
            .filter(m -> m.getMetadata().getId().equals(id))
            .findFirst();
    }
    
    /**
     * 检查 Mod 是否已加载
     */
    public boolean isModLoaded(String id) {
        return getModContainer(id).isPresent();
    }
}
