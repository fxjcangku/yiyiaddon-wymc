package com.yiyiaddon.wymc.launcher;

import com.yiyiaddon.wymc.launcher.ui.fx.FxMainWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WymcLauncher {
    private static final Logger LOGGER = LoggerFactory.getLogger(WymcLauncher.class);
    
    public static void main(String[] args) {
        try {
            LOGGER.info("YiYi WYMC Loader started");
            
            // 启动 JavaFX UI
            FxMainWindow.launch();
        } catch (Exception e) {
            LOGGER.error("启动失败", e);
            System.exit(1);
        }
    }
}
