package com.yiyiaddon.wymc.runtime.fabric;

/**
 * Fake Mod Metadata
 * 
 * 伪装 Fabric 的 ModMetadata
 */
public class FakeModMetadata {
    
    private final String id;
    private final String version;
    private final String name;
    private final String description;
    
    public FakeModMetadata(String id, String version, String name, String description) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.description = description;
    }
    
    public String getId() {
        return id;
    }
    
    public String getVersion() {
        return version;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
}
