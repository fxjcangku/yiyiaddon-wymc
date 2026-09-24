package com.yiyiaddon.wymc.agent;

import java.lang.instrument.Instrumentation;

/**
 * Runtime Context - 运行时上下文
 * 
 * 保存 Agent 运行时的全局状态
 */
public class RuntimeContext {
    
    private final Instrumentation instrumentation;
    private Object minecraftInstance;
    
    public RuntimeContext(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }
    
    public Instrumentation getInstrumentation() {
        return instrumentation;
    }
    
    public void setMinecraftInstance(Object instance) {
        this.minecraftInstance = instance;
    }
    
    public Object getMinecraftInstance() {
        return minecraftInstance;
    }
}
