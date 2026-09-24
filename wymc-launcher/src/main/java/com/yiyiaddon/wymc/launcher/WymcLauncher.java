package com.yiyiaddon.wymc.launcher;

import com.yiyiaddon.wymc.launcher.ui.MainWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WymcLauncher {
    private static final Logger LOGGER = LoggerFactory.getLogger(WymcLauncher.class);
    
    public static void main(String[] args) {
        try {
            setupEnvironment();
            
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            SwingUtilities.invokeLater(() -> {
                MainWindow window = new MainWindow();
                window.setVisible(true);
            });
            
            LOGGER.info("YiYi WYMC Loader started");
        } catch (Exception e) {
            LOGGER.error("Failed to start WYMC Loader", e);
            JOptionPane.showMessageDialog(null, 
                "启动失败: " + e.getMessage(), 
                "YiYi WYMC Loader", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    private static void setupEnvironment() throws Exception {
        Path baseDir = Paths.get(System.getProperty("user.home"), ".yiyiaddon-wymc");
        Files.createDirectories(baseDir);
        
        Path logsDir = baseDir.resolve("logs");
        Files.createDirectories(logsDir);
        
        Path reportsDir = baseDir.resolve("reports");
        Files.createDirectories(reportsDir);
        
        Path runtimeDumpDir = baseDir.resolve("runtime-dump");
        Files.createDirectories(runtimeDumpDir);
        
        System.setProperty("wymc.base.dir", baseDir.toString());
        System.setProperty("wymc.logs.dir", logsDir.toString());
        System.setProperty("wymc.reports.dir", reportsDir.toString());
        System.setProperty("wymc.dump.dir", runtimeDumpDir.toString());
    }
}
