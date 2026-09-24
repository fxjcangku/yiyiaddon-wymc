package com.yiyiaddon.wymc.launcher.ui;

import com.yiyiaddon.wymc.launcher.ui.components.*;
import com.yiyiaddon.wymc.launcher.process.MinecraftProcessDetector;
import com.yiyiaddon.wymc.launcher.process.ProcessCandidate;
import com.yiyiaddon.wymc.launcher.environment.MinecraftVersionDetector;
import com.yiyiaddon.wymc.launcher.analysis.runtime.RuntimeAnalyzer;
import com.yiyiaddon.wymc.launcher.attach.AgentAttacher;
import com.yiyiaddon.wymc.launcher.attach.AttachResult;
import com.yiyiaddon.wymc.launcher.report.ReportGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 * 主窗口 - 玻璃质感版本
 */
public class MainWindow extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindow.class);
    
    // Minecraft 信息
    private InfoLabel statusLabel;
    private InfoLabel pidLabel;
    private InfoLabel versionLabel;
    private InfoLabel gameDirLabel;
    
    // Java 信息
    private InfoLabel javaVersionLabel;
    private InfoLabel javaVmLabel;
    private InfoLabel javaArchLabel;
    
    // Runtime 信息
    private InfoLabel neteaseLabel;
    private InfoLabel mappingLabel;
    private InfoLabel obfuscationLabel;
    private InfoLabel classLoaderLabel;
    
    // Unpack 信息
    private InfoLabel runtimeClassesLabel;
    private InfoLabel runtimeRecoveryLabel;
    private InfoLabel classDumpLabel;
    
    // Injection 信息
    private InfoLabel attachLabel;
    private InfoLabel instrumentationLabel;
    private InfoLabel agentStatusLabel;
    
    // Server 信息
    private InfoLabel serverConnectedLabel;
    private InfoLabel serverBrandLabel;
    private InfoLabel serverProtocolLabel;
    
    // 按钮
    private GlassButton detectButton;
    private GlassButton analyzeButton;
    private GlassButton attachButton;
    private GlassButton reportButton;
    private GlassButton logsButton;
    
    // 数据
    private ProcessCandidate currentProcess;
    private MinecraftVersionDetector.VersionInfo currentVersion;
    private RuntimeAnalyzer.RuntimeAnalysisResult runtimeAnalysis;
    
    public MainWindow() {
        initModernUI();
    }
    
    private void initModernUI() {
        setTitle("YiYi WYMC Loader");
        setSize(750, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // 主面板 - 浅灰背景
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 标题栏
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        
        // 内容区域（玻璃面板）
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        
        // 按钮区域
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // 自动检测
        SwingUtilities.invokeLater(this::autoDetect);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("YiYi WYMC Loader");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        JLabel versionLabel = new JLabel(" v1.0.0-PHASE1");
        versionLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        versionLabel.setForeground(new Color(150, 150, 150));
        
        panel.add(titleLabel);
        panel.add(versionLabel);
        return panel;
    }
    
    private JScrollPane createContentPanel() {
        GlassPanel glassPanel = new GlassPanel();
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        
        // Minecraft 区域
        SectionPanel minecraftSection = new SectionPanel("Minecraft");
        statusLabel = new InfoLabel("状态:", "WAITING_FOR_MINECRAFT");
        statusLabel.setValueColor(new Color(255, 149, 0));
        pidLabel = new InfoLabel("PID:", "N/A");
        versionLabel = new InfoLabel("版本:", "N/A");
        gameDirLabel = new InfoLabel("游戏目录:", "N/A");
        
        minecraftSection.addInfo(statusLabel);
        minecraftSection.addInfo(pidLabel);
        minecraftSection.addInfo(versionLabel);
        minecraftSection.addInfo(gameDirLabel);
        
        glassPanel.add(minecraftSection);
        glassPanel.add(Box.createVerticalStrut(15));
        
        // Java 区域
        SectionPanel javaSection = new SectionPanel("Java");
        javaVersionLabel = new InfoLabel("Version:", "N/A");
        javaVmLabel = new InfoLabel("VM:", "N/A");
        javaArchLabel = new InfoLabel("Architecture:", "N/A");
        
        javaSection.addInfo(javaVersionLabel);
        javaSection.addInfo(javaVmLabel);
        javaSection.addInfo(javaArchLabel);
        
        glassPanel.add(javaSection);
        glassPanel.add(Box.createVerticalStrut(15));
        
        // Runtime 区域
        SectionPanel runtimeSection = new SectionPanel("Runtime");
        neteaseLabel = new InfoLabel("NetEase:", "N/A");
        mappingLabel = new InfoLabel("Mapping:", "N/A");
        obfuscationLabel = new InfoLabel("Obfuscation:", "N/A");
        classLoaderLabel = new InfoLabel("ClassLoader:", "N/A");
        
        runtimeSection.addInfo(neteaseLabel);
        runtimeSection.addInfo(mappingLabel);
        runtimeSection.addInfo(obfuscationLabel);
        runtimeSection.addInfo(classLoaderLabel);
        
        glassPanel.add(runtimeSection);
        glassPanel.add(Box.createVerticalStrut(15));
        
        // Unpack 区域
        SectionPanel unpackSection = new SectionPanel("Unpack");
        runtimeClassesLabel = new InfoLabel("Runtime Classes:", "N/A");
        runtimeRecoveryLabel = new InfoLabel("Runtime Recovery:", "N/A");
        classDumpLabel = new InfoLabel("Class Dump:", "N/A");
        
        unpackSection.addInfo(runtimeClassesLabel);
        unpackSection.addInfo(runtimeRecoveryLabel);
        unpackSection.addInfo(classDumpLabel);
        
        glassPanel.add(unpackSection);
        glassPanel.add(Box.createVerticalStrut(15));
        
        // Injection 区域
        SectionPanel injectionSection = new SectionPanel("Injection");
        attachLabel = new InfoLabel("Attach:", "N/A");
        instrumentationLabel = new InfoLabel("Instrumentation:", "N/A");
        agentStatusLabel = new InfoLabel("Agent:", "N/A");
        
        injectionSection.addInfo(attachLabel);
        injectionSection.addInfo(instrumentationLabel);
        injectionSection.addInfo(agentStatusLabel);
        
        glassPanel.add(injectionSection);
        glassPanel.add(Box.createVerticalStrut(15));
        
        // Server 区域
        SectionPanel serverSection = new SectionPanel("Server");
        serverConnectedLabel = new InfoLabel("Connected:", "N/A");
        serverBrandLabel = new InfoLabel("Brand:", "N/A");
        serverProtocolLabel = new InfoLabel("Protocol:", "N/A");
        
        serverSection.addInfo(serverConnectedLabel);
        serverSection.addInfo(serverBrandLabel);
        serverSection.addInfo(serverProtocolLabel);
        
        glassPanel.add(serverSection);
        
        JScrollPane scrollPane = new JScrollPane(glassPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        return scrollPane;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setOpaque(false);
        
        detectButton = new GlassButton("重新检测");
        detectButton.addActionListener(this::onDetect);
        
        analyzeButton = new GlassButton("分析 Runtime");
        analyzeButton.setEnabled(false);
        analyzeButton.addActionListener(this::onAnalyze);
        
        attachButton = new GlassButton("加载 Agent");
        attachButton.setEnabled(false);
        attachButton.setButtonColor(
            new Color(52, 199, 89, 220),
            new Color(72, 219, 109, 240),
            new Color(32, 179, 69, 220)
        );
        attachButton.addActionListener(this::onAttach);
        
        reportButton = new GlassButton("导出报告");
        reportButton.setEnabled(false);
        reportButton.addActionListener(this::onExportReport);
        
        logsButton = new GlassButton("打开日志");
        logsButton.setButtonColor(
            new Color(150, 150, 150, 200),
            new Color(170, 170, 170, 220),
            new Color(130, 130, 130, 200)
        );
        logsButton.addActionListener(this::onOpenLogs);
        
        panel.add(detectButton);
        panel.add(analyzeButton);
        panel.add(attachButton);
        panel.add(reportButton);
        panel.add(logsButton);
        
        return panel;
    }
    
    private void autoDetect() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
                detectMinecraft();
            } catch (Exception e) {
                LOGGER.error("自动检测失败", e);
            }
        }).start();
    }
    
    private void onDetect(ActionEvent e) {
        detectMinecraft();
    }
    
    private void detectMinecraft() {
        LOGGER.info("开始检测 Minecraft");
        
        MinecraftProcessDetector detector = new MinecraftProcessDetector();
        List<ProcessCandidate> candidates = detector.detectMinecraftProcesses();
        
        if (candidates.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setValue("WAITING_FOR_MINECRAFT");
                statusLabel.setValueColor(new Color(255, 149, 0));
                JOptionPane.showMessageDialog(this, 
                    "未找到 Minecraft 进程\n请先启动网易 Minecraft", 
                    "检测结果", 
                    JOptionPane.WARNING_MESSAGE);
            });
            return;
        }
        
        currentProcess = candidates.get(0);
        
        MinecraftVersionDetector versionDetector = new MinecraftVersionDetector();
        currentVersion = versionDetector.detectVersion(currentProcess);
        
        SwingUtilities.invokeLater(() -> {
            statusLabel.setValue("DETECTED");
            statusLabel.setValueColor(new Color(52, 199, 89));
            pidLabel.setValue(String.valueOf(currentProcess.getPid()));
            versionLabel.setValue(currentVersion.getVersion());
            gameDirLabel.setValue(truncate(currentProcess.getWorkingDirectory(), 35));
            javaVersionLabel.setValue(currentProcess.getJavaVersion());
            javaVmLabel.setValue("HotSpot");
            javaArchLabel.setValue(System.getProperty("os.arch"));
            
            analyzeButton.setEnabled(true);
            attachButton.setEnabled(true);
        });
        
        LOGGER.info("检测完成: PID={}, Version={}", currentProcess.getPid(), currentVersion.getVersion());
    }
    
    private void onAnalyze(ActionEvent e) {
        if (currentProcess == null) {
            JOptionPane.showMessageDialog(this, "请先检测 Minecraft 进程", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        new Thread(() -> {
            try {
                LOGGER.info("开始分析 Runtime");
                
                RuntimeAnalyzer analyzer = new RuntimeAnalyzer();
                runtimeAnalysis = analyzer.analyze(
                    currentProcess.getWorkingDirectory(),
                    currentProcess.getClasspath()
                );
                
                SwingUtilities.invokeLater(() -> {
                    neteaseLabel.setValue(runtimeAnalysis.getPackagingType().toString());
                    mappingLabel.setValue(runtimeAnalysis.getObfuscationType().toString());
                    obfuscationLabel.setValue(runtimeAnalysis.getObfuscationType() != 
                        RuntimeAnalyzer.ObfuscationType.NONE ? "YES" : "NO");
                    classLoaderLabel.setValue(runtimeAnalysis.hasCustomClassLoader() ? "CUSTOM" : "STANDARD");
                    
                    reportButton.setEnabled(true);
                    
                    JOptionPane.showMessageDialog(MainWindow.this, 
                        "Runtime 分析完成", 
                        "分析结果", 
                        JOptionPane.INFORMATION_MESSAGE);
                });
                
                LOGGER.info("Runtime 分析完成");
                
            } catch (Exception ex) {
                LOGGER.error("分析失败", ex);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(MainWindow.this, 
                        "分析失败: " + ex.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }
    
    private void onAttach(ActionEvent e) {
        if (currentProcess == null) {
            JOptionPane.showMessageDialog(this, "请先检测 Minecraft 进程", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        new Thread(() -> {
            try {
                LOGGER.info("开始附加 Agent");
                
                java.nio.file.Path agentJar = Paths.get("build", "libs", "wymc-agent.jar");
                
                if (!agentJar.toFile().exists()) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(MainWindow.this, 
                            "Agent JAR 不存在:\n" + agentJar.toAbsolutePath() + "\n\n请先编译项目", 
                            "错误", 
                            JOptionPane.ERROR_MESSAGE);
                    });
                    return;
                }
                
                AgentAttacher attacher = new AgentAttacher();
                AttachResult result = attacher.attach(currentProcess.getPid(), agentJar);
                
                SwingUtilities.invokeLater(() -> {
                    if (result.isSuccess()) {
                        attachLabel.setValue("SUCCESS");
                        attachLabel.setValueColor(new Color(52, 199, 89));
                        instrumentationLabel.setValue("AVAILABLE");
                        instrumentationLabel.setValueColor(new Color(52, 199, 89));
                        agentStatusLabel.setValue("LOADED");
                        agentStatusLabel.setValueColor(new Color(52, 199, 89));
                        
                        JOptionPane.showMessageDialog(MainWindow.this, 
                            "Agent 加载成功\n请查看控制台输出", 
                            "成功", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        attachLabel.setValue("FAILED");
                        attachLabel.setValueColor(new Color(255, 59, 48));
                        
                        JOptionPane.showMessageDialog(MainWindow.this, 
                            "Agent 加载失败:\n" + result.getMessage(), 
                            "错误", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                });
                
            } catch (Exception ex) {
                LOGGER.error("附加 Agent 失败", ex);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(MainWindow.this, 
                        "附加失败: " + ex.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }
    
    private void onExportReport(ActionEvent e) {
        new Thread(() -> {
            try {
                LOGGER.info("开始导出报告");
                
                ReportGenerator generator = new ReportGenerator();
                
                ReportGenerator.EnvironmentData envData = new ReportGenerator.EnvironmentData();
                if (currentProcess != null) {
                    envData.pid = currentProcess.getPid();
                    envData.javaInfo.put("version", currentProcess.getJavaVersion());
                    envData.javaInfo.put("vm", "HotSpot");
                    envData.minecraftInfo.put("version", currentVersion != null ? currentVersion.getVersion() : "UNKNOWN");
                    envData.minecraftInfo.put("gameDir", currentProcess.getWorkingDirectory());
                }
                generator.generateEnvironmentReport(envData);
                
                if (runtimeAnalysis != null) {
                    ReportGenerator.RuntimeUnpackData unpackData = new ReportGenerator.RuntimeUnpackData();
                    unpackData.packagingDetected = runtimeAnalysis.getPackagingType().toString();
                    unpackData.obfuscationDetected = runtimeAnalysis.getObfuscationType().toString();
                    unpackData.customClassLoader = runtimeAnalysis.hasCustomClassLoader();
                    unpackData.runtimeTransformation = runtimeAnalysis.hasRuntimeTransformation();
                    generator.generateRuntimeUnpackReport(unpackData);
                }
                
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(MainWindow.this, 
                        "报告已导出到:\n" + System.getProperty("wymc.reports.dir"), 
                        "成功", 
                        JOptionPane.INFORMATION_MESSAGE);
                });
                
                LOGGER.info("报告导出完成");
                
            } catch (Exception ex) {
                LOGGER.error("导出报告失败", ex);
            }
        }).start();
    }
    
    private void onOpenLogs(ActionEvent e) {
        try {
            String logsDir = System.getProperty("wymc.logs.dir");
            Desktop.getDesktop().open(new File(logsDir));
        } catch (Exception ex) {
            LOGGER.error("打开日志目录失败", ex);
        }
    }
    
    private String truncate(String str, int maxLen) {
        if (str == null || str.length() <= maxLen) return str != null ? str : "N/A";
        return "..." + str.substring(str.length() - maxLen);
    }
}
