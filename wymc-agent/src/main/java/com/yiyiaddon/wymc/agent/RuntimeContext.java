package com.yiyiaddon.wymc.agent;

import java.lang.instrument.Instrumentation;
import java.util.*;

public class RuntimeContext {
    private final Instrumentation instrumentation;
    private final Map<String, Object> properties;
    private final long attachTime;
    
    public RuntimeContext(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        this.properties = new HashMap<>();
        this.attachTime = System.currentTimeMillis();
        
        analyzeCapabilities();
    }
    
    private void analyzeCapabilities() {
        properties.put("can_redefine", instrumentation.isRedefineClassesSupported());
        properties.put("can_retransform", instrumentation.isRetransformClassesSupported());
        properties.put("can_set_native_prefix", instrumentation.isNativeMethodPrefixSupported());
        
        Class<?>[] loadedClasses = instrumentation.getAllLoadedClasses();
        properties.put("loaded_classes_count", loadedClasses.length);
        
        System.out.println("[WYMC] Runtime capabilities analyzed:");
        System.out.println("  - Redefine: " + properties.get("can_redefine"));
        System.out.println("  - Retransform: " + properties.get("can_retransform"));
        System.out.println("  - Native prefix: " + properties.get("can_set_native_prefix"));
        System.out.println("  - Loaded classes: " + properties.get("loaded_classes_count"));
    }
    
    public Instrumentation getInstrumentation() {
        return instrumentation;
    }
    
    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }
    
    public long getAttachTime() {
        return attachTime;
    }
    
    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }
    
    public Object getProperty(String key) {
        return properties.get(key);
    }
}
