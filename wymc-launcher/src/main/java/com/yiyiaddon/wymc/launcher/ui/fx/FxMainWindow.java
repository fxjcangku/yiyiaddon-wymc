package com.yiyiaddon.wymc.launcher.ui.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiyiaddon.wymc.launcher.process.MinecraftProcessDetector;
import com.yiyiaddon.wymc.launcher.process.ProcessCandidate;

import java.util.List;

/**
 * JavaFX 主窗口
 */
public class FxMainWindow extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(FxMainWindow.class);
    
    // 信息标签
    private Label statusLabel;
    private Label pidLabel;
    private Label versionLabel;
    private Label gameDirLabel;
    
    private Label javaVersionLabel;
    private Label javaVmLabel;
    private Label javaArchLabel;
    
    private Label netEaseLabel;
    private Label mappingLabel;
    private Label obfuscationLabel;
    private Label classLoaderLabel;
    
    private Label runtimeClassesLabel;
    private Label runtimeRecoveryLabel;
    private Label classDumpLabel;
    
    private Label attachLabel;
    private Label instrumentationLabel;
    private Label agentLabel;
    
    private Label serverConnectedLabel;
    private Label serverBrandLabel;
    private Label serverProtocolLabel;
    private Label serverAddressLabel;
    private Label serverPortLabel;
    private Label serverLatencyLabel;
    
    // 按钮
    private Button detectButton;
    private Button analyzeButton;
    private Button attachButton;
    private Button exportButton;
    private Button logButton;
    
    private ProcessCandidate currentProcess;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("YiYi WYMC Loader");
        primaryStage.initStyle(StageStyle.DECORATED);
        
        VBox root = createMainLayout();
        
        Scene scene = new Scene(root, 900, 900);
        scene.getStylesheets().add(
            getClass().getResource("/css/wymc-dark.css").toExternalForm()
        );
        scene.setFill(Color.TRANSPARENT);
        
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        // 自动检测
        autoDetect();
    }
    
    private VBox createMainLayout() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.getStyleClass().add("root");
        
        // 标题
        Label title = new Label("YiYi WYMC Loader");
        title.getStyleClass().add("title");
        
        Label subtitle = new Label("v1.0.0-PHASE2 · 网易 Minecraft 运行时分析与注入");
        subtitle.getStyleClass().add("subtitle");
        
        VBox header = new VBox(5, title, subtitle);
        header.setAlignment(Pos.CENTER);
        
        // 信息面板（可滚动）
        ScrollPane scrollPane = new ScrollPane(createInfoPanel());
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("info-scroll");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        // 按钮区
        HBox buttonBox = createButtonBox();
        
        root.getChildren().addAll(header, scrollPane, buttonBox);
        return root;
    }
    
    private VBox createInfoPanel() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(5));
        
        panel.getChildren().addAll(
            createMinecraftSection(),
            createJavaSection(),
            createRuntimeSection(),
            createUnpackSection(),
            createInjectionSection(),
            createServerSection()
        );
        
        return panel;
    }
    
    private VBox createMinecraftSection() {
        statusLabel = createValueLabel("WAITING_FOR_MINECRAFT", "status-waiting");
        pidLabel = createValueLabel("N/A");
        versionLabel = createValueLabel("N/A");
        gameDirLabel = createValueLabel("N/A");
        
        return createSection("Minecraft",
            createInfoRow("状态:", statusLabel),
            createInfoRow("PID:", pidLabel),
            createInfoRow("版本:", versionLabel),
            createInfoRow("游戏目录:", gameDirLabel)
        );
    }
    
    private VBox createJavaSection() {
        javaVersionLabel = createValueLabel("N/A");
        javaVmLabel = createValueLabel("N/A");
        javaArchLabel = createValueLabel("N/A");
        
        return createSection("Java",
            createInfoRow("Version:", javaVersionLabel),
            createInfoRow("VM:", javaVmLabel),
            createInfoRow("Architecture:", javaArchLabel)
        );
    }
    
    private VBox createRuntimeSection() {
        netEaseLabel = createValueLabel("N/A");
        mappingLabel = createValueLabel("N/A");
        obfuscationLabel = createValueLabel("N/A");
        classLoaderLabel = createValueLabel("N/A");
        
        return createSection("Runtime",
            createInfoRow("NetEase:", netEaseLabel),
            createInfoRow("Mapping:", mappingLabel),
            createInfoRow("Obfuscation:", obfuscationLabel),
            createInfoRow("ClassLoader:", classLoaderLabel)
        );
    }
    
    private VBox createUnpackSection() {
        runtimeClassesLabel = createValueLabel("N/A");
        runtimeRecoveryLabel = createValueLabel("N/A");
        classDumpLabel = createValueLabel("N/A");
        
        return createSection("Unpack",
            createInfoRow("Runtime Classes:", runtimeClassesLabel),
            createInfoRow("Runtime Recovery:", runtimeRecoveryLabel),
            createInfoRow("Class Dump:", classDumpLabel)
        );
    }
    
    private VBox createInjectionSection() {
        attachLabel = createValueLabel("N/A");
        instrumentationLabel = createValueLabel("N/A");
        agentLabel = createValueLabel("N/A");
        
        return createSection("Injection",
            createInfoRow("Attach:", attachLabel),
            createInfoRow("Instrumentation:", instrumentationLabel),
            createInfoRow("Agent:", agentLabel)
        );
    }
    
    private VBox createServerSection() {
        serverConnectedLabel = createValueLabel("N/A");
        serverBrandLabel = createValueLabel("N/A");
        serverProtocolLabel = createValueLabel("N/A");
        serverAddressLabel = createValueLabel("N/A");
        serverPortLabel = createValueLabel("N/A");
        serverLatencyLabel = createValueLabel("N/A");
        
        return createSection("Server",
            createInfoRow("Connected:", serverConnectedLabel),
            createInfoRow("Brand:", serverBrandLabel),
            createInfoRow("Protocol:", serverProtocolLabel),
            createInfoRow("Address:", serverAddressLabel),
            createInfoRow("Port:", serverPortLabel),
            createInfoRow("Latency:", serverLatencyLabel)
        );
    }
    
    private VBox createSection(String title, HBox... rows) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");
        
        VBox content = new VBox(8);
        content.getStyleClass().add("section-content");
        content.getChildren().addAll(rows);
        
        VBox section = new VBox(10, titleLabel, content);
        section.getStyleClass().add("section");
        
        return section;
    }
    
    private HBox createInfoRow(String key, Label value) {
        Label keyLabel = new Label(key);
        keyLabel.getStyleClass().add("info-key");
        keyLabel.setMinWidth(180);
        
        value.getStyleClass().add("info-value");
        
        HBox row = new HBox(15, keyLabel, value);
        row.setAlignment(Pos.CENTER_LEFT);
        
        return row;
    }
    
    private Label createValueLabel(String text) {
        return new Label(text);
    }
    
    private Label createValueLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }
    
    private HBox createButtonBox() {
        detectButton = createButton("重新检测");
        detectButton.setOnAction(e -> onDetect());
        
        analyzeButton = createButton("脱壳分析");
        analyzeButton.setDisable(true);
        analyzeButton.setOnAction(e -> onAnalyze());
        
        attachButton = createButton("加载 Agent");
        attachButton.setDisable(true);
        attachButton.setOnAction(e -> onAttach());
        
        exportButton = createButton("导出报告");
        exportButton.setDisable(true);
        exportButton.setOnAction(e -> onExport());
        
        logButton = createButton("打开日志");
        logButton.setOnAction(e -> onOpenLog());
        
        HBox box = new HBox(15, detectButton, analyzeButton, attachButton, exportButton, logButton);
        box.setAlignment(Pos.CENTER);
        
        return box;
    }
    
    private Button createButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("action-button");
        button.setMinWidth(120);
        return button;
    }
    
    private void autoDetect() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
                detectMinecraft(true);
            } catch (Exception e) {
                LOGGER.error("自动检测失败", e);
            }
        }).start();
    }
    
    private void onDetect() {
        detectMinecraft(false);
    }
    
    private void detectMinecraft(boolean silent) {
        LOGGER.info("开始检测 Minecraft");
        
        MinecraftProcessDetector detector = new MinecraftProcessDetector();
        List<ProcessCandidate> candidates = detector.detectMinecraftProcesses();
        
        if (candidates.isEmpty()) {
            Platform.runLater(() -> {
                updateStatus("WAITING_FOR_MINECRAFT", "status-waiting");
                if (!silent) {
                    // TODO: 显示对话框
                }
            });
            return;
        }
        
        currentProcess = candidates.get(0);
        Platform.runLater(() -> updateProcessInfo(currentProcess));
    }
    
    private void updateStatus(String text, String styleClass) {
        statusLabel.setText(text);
        statusLabel.getStyleClass().removeAll("status-waiting", "status-detected", "status-error");
        statusLabel.getStyleClass().add(styleClass);
    }
    
    private void updateProcessInfo(ProcessCandidate process) {
        updateStatus("DETECTED", "status-detected");
        pidLabel.setText(String.valueOf(process.getPid()));
        
        analyzeButton.setDisable(false);
        attachButton.setDisable(false);
        exportButton.setDisable(false);
    }
    
    private void onAnalyze() {
        LOGGER.info("开始分析 Runtime");
        // TODO: 实现分析逻辑
    }
    
    private void onAttach() {
        LOGGER.info("开始加载 Agent");
        // TODO: 实现注入逻辑
    }
    
    private void onExport() {
        LOGGER.info("导出报告");
        // TODO: 实现导出逻辑
    }
    
    private void onOpenLog() {
        LOGGER.info("打开日志");
        // TODO: 实现打开日志逻辑
    }
    
    public static void launch() {
        Application.launch(FxMainWindow.class);
    }
}
