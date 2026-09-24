package com.yiyiaddon.wymc.runtime.fabric;

/**
 * Fake Mod Container
 * 
 * 伪装 Fabric 的 ModContainer
 */
public class FakeModContainer {
    
    private final FakeModMetadata metadata;
    private final Object modInstance;
    
    public FakeModContainer(FakeModMetadata metadata, Object modInstance) {
        this.metadata = metadata;
        this.modInstance = modInstance;
    }
    
    public FakeModMetadata getMetadata() {
        return metadata;
    }
    
    public Object getModInstance() {
        return modInstance;
    }
}
