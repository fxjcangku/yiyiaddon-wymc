package com.yiyiaddon.wymc.launcher.attach;

import com.sun.tools.attach.VirtualMachine;

public class AttachResult {
    private final boolean success;
    private final String message;
    private final VirtualMachine vm;
    
    public AttachResult(boolean success, String message, VirtualMachine vm) {
        this.success = success;
        this.message = message;
        this.vm = vm;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public VirtualMachine getVirtualMachine() {
        return vm;
    }
}
