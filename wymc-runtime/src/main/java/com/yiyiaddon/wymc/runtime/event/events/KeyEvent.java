package com.yiyiaddon.wymc.runtime.event.events;

import com.yiyiaddon.wymc.runtime.event.Event;

/**
 * KeyEvent - 键盘事件
 */
public class KeyEvent extends Event {
    
    private final int key;
    private final int action;
    
    public KeyEvent(int key, int action) {
        this.key = key;
        this.action = action;
    }
    
    public int getKey() {
        return key;
    }
    
    public int getAction() {
        return action;
    }
    
    @Override
    public boolean isCancellable() {
        return true;
    }
}
