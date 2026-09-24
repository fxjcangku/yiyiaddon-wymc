package com.yiyiaddon.wymc.agent;

import com.yiyiaddon.wymc.agent.analysis.ClassLoaderAnalyzer;
import com.yiyiaddon.wymc.agent.analysis.MinecraftClassProbe;

import java.lang.instrument.Instrumentation;

public class AgentBootstrap {
    
    public static void bootstrap(Instrumentation inst) throws Exception {
        System.out.println("[WYMC] Starting agent bootstrap");
        
        // 分析 ClassLoader 结构
        ClassLoaderAnalyzer classLoaderAnalyzer = new ClassLoaderAnalyzer(inst);
        classLoaderAnalyzer.analyze();
        
        // 探测 Minecraft 类
        MinecraftClassProbe minecraftProbe = new MinecraftClassProbe(inst);
        minecraftProbe.probe();
        
        System.out.println("[WYMC] Bootstrap completed");
    }
}
