package com.yiyiaddon.wymc.launcher.attach;

import com.sun.tools.attach.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AgentAttacher {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentAttacher.class);
    
    public AttachResult attach(long pid, Path agentJarPath) {
        LOGGER.info("开始附加 Agent 到进程 {}", pid);
        
        if (!agentJarPath.toFile().exists()) {
            String error = "Agent JAR 不存在: " + agentJarPath;
            LOGGER.error(error);
            return new AttachResult(false, error, null);
        }
        
        try {
            String pidStr = String.valueOf(pid);
            VirtualMachine vm = VirtualMachine.attach(pidStr);
            
            try {
                LOGGER.info("已连接到 JVM (PID: {})", pid);
                
                vm.loadAgent(agentJarPath.toString());
                
                LOGGER.info("Agent 加载成功");
                
                return new AttachResult(true, "Agent attached successfully", vm);
                
            } catch (Exception e) {
                vm.detach();
                throw e;
            }
            
        } catch (Exception e) {
            String error = "附加 Agent 失败: " + e.getMessage();
            LOGGER.error(error, e);
            return new AttachResult(false, error, null);
        }
    }
}
