package com.yiyiaddon.wymc.agent;

import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

public class WymcAgent {
    private static Instrumentation instrumentation;
    private static RuntimeContext runtimeContext;
    
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("[WYMC] Agent attached successfully");
        
        instrumentation = inst;
        runtimeContext = new RuntimeContext(inst);
        
        System.out.println("[WYMC] Instrumentation initialized");
        
        try {
            AgentBootstrap.bootstrap(inst);
            System.out.println("[WYMC] Runtime loaded successfully");
        } catch (Exception e) {
            System.err.println("[WYMC] Agent bootstrap failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
    
    public static RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }
}
