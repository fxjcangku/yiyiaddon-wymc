# Swing 玻璃质感 UI 实现指南

**参考目标**: iOS 毛玻璃效果  
**技术栈**: Java Swing  
**适用项目**: YiYi WYMC Loader

---

## 一、效果预览

**目标效果**：
- 半透明白色背景
- 圆角边框
- 轻微阴影
- 渐变色装饰
- 现代感按钮

**Swing 限制**：
- 无法实现真正的背景模糊（需要 native 支持）
- 可以模拟半透明 + 阴影
- 性能考虑：避免复杂渲染

---

## 二、基础玻璃面板

```java
package com.yiyiaddon.wymc.launcher.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * 玻璃质感面板
 * 半透明白色背景 + 圆角 + 阴影
 */
public class GlassPanel extends JPanel {
    
    private Color backgroundColor = new Color(255, 255, 255, 220);
    private Color borderColor = new Color(200, 200, 200, 150);
    private int cornerRadius = 15;
    
    public GlassPanel() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // 阴影效果（简化版）
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillRoundRect(2, 2, getWidth()-2, getHeight()-2, cornerRadius, cornerRadius);
        
        // 玻璃背景
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, cornerRadius, cornerRadius);
        
        // 边框
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(0, 0, getWidth()-2, getHeight()-2, cornerRadius, cornerRadius);
        
        g2d.dispose();
        super.paintComponent(g);
    }
    
    public void setGlassColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }
}
```

---

## 三、现代按钮

```java
package com.yiyiaddon.wymc.launcher.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 玻璃质感按钮
 */
public class GlassButton extends JButton {
    
    private Color normalColor = new Color(100, 150, 255, 200);
    private Color hoverColor = new Color(120, 170, 255, 230);
    private Color pressedColor = new Color(80, 130, 235, 200);
    private Color currentColor;
    
    private boolean isHovered = false;
    private boolean isPressed = false;
    
    public GlassButton(String text) {
        super(text);
        currentColor = normalColor;
        
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(new Font("微软雅黑", Font.PLAIN, 14));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                currentColor = hoverColor;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                currentColor = normalColor;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                currentColor = pressedColor;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                currentColor = isHovered ? hoverColor : normalColor;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 按钮背景
        g2d.setColor(currentColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        
        // 高光效果
        if (isHovered && !isPressed) {
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(255, 255, 255, 60),
                0, getHeight() / 2, new Color(255, 255, 255, 0)
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight() / 2, 10, 10);
        }
        
        g2d.dispose();
        super.paintComponent(g);
    }
}
```

---

## 四、升级 MainWindow

```java
package com.yiyiaddon.wymc.launcher.ui;

import com.yiyiaddon.wymc.launcher.ui.components.*;
import javax.swing.*;
import java.awt.*;

public class ModernMainWindow extends JFrame {
    
    public ModernMainWindow() {
        initModernUI();
    }
    
    private void initModernUI() {
        setTitle("YiYi WYMC Loader");
        setSize(750, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // 去掉默认边框（可选）
        // setUndecorated(true);
        
        // 主面板（深色背景）
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 标题栏
        JPanel titlePanel = createTitlePanel();
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // 内容区域（使用玻璃面板）
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // 按钮区域
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("YiYi WYMC Loader");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        panel.add(titleLabel);
        return panel;
    }
    
    private JPanel createContentPanel() {
        GlassPanel glassPanel = new GlassPanel();
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        
        // 添加各个信息区块
        glassPanel.add(createInfoSection("Minecraft", new String[][]{
            {"状态:", "WAITING_FOR_MINECRAFT"},
            {"PID:", "N/A"},
            {"版本:", "N/A"}
        }));
        
        glassPanel.add(Box.createVerticalStrut(10));
        
        glassPanel.add(createInfoSection("Java", new String[][]{
            {"Version:", "N/A"},
            {"VM:", "N/A"},
            {"Architecture:", "N/A"}
        }));
        
        glassPanel.add(Box.createVerticalStrut(10));
        
        glassPanel.add(createInfoSection("Runtime", new String[][]{
            {"NetEase:", "N/A"},
            {"Mapping:", "N/A"},
            {"Obfuscation:", "N/A"}
        }));
        
        return glassPanel;
    }
    
    private JPanel createInfoSection(String title, String[][] fields) {
        JPanel section = new JPanel();
        section.setOpaque(false);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200, 100), 1),
            title,
            0,
            0,
            new Font("微软雅黑", Font.BOLD, 14),
            new Color(80, 80, 80)
        ));
        
        for (String[] field : fields) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            row.setOpaque(false);
            
            JLabel keyLabel = new JLabel(field[0]);
            keyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            keyLabel.setForeground(new Color(100, 100, 100));
            
            JLabel valueLabel = new JLabel(field[1]);
            valueLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
            valueLabel.setForeground(new Color(50, 50, 50));
            
            row.add(keyLabel);
            row.add(valueLabel);
            section.add(row);
        }
        
        return section;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setOpaque(false);
        
        panel.add(new GlassButton("重新检测"));
        panel.add(new GlassButton("分析 Runtime"));
        panel.add(new GlassButton("加载 Agent"));
        panel.add(new GlassButton("导出报告"));
        panel.add(new GlassButton("打开日志"));
        
        return panel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ModernMainWindow().setVisible(true);
        });
    }
}
```

---

## 五、颜色方案

### 推荐配色（iOS 风格）

```java
// 背景色
Color lightBackground = new Color(240, 242, 245);      // 浅灰背景
Color glassBackground = new Color(255, 255, 255, 220); // 玻璃白

// 主色调（蓝色系）
Color primaryNormal = new Color(0, 122, 255, 200);     // iOS 蓝
Color primaryHover = new Color(30, 142, 255, 230);
Color primaryPressed = new Color(0, 102, 235, 200);

// 成功色（绿色）
Color successColor = new Color(52, 199, 89, 200);

// 警告色（橙色）
Color warningColor = new Color(255, 149, 0, 200);

// 错误色（红色）
Color errorColor = new Color(255, 59, 48, 200);

// 文字色
Color textPrimary = new Color(50, 50, 50);
Color textSecondary = new Color(120, 120, 120);
Color textDisabled = new Color(200, 200, 200);
```

---

## 六、使用建议

### 时机
- ✅ Phase 1.5: Launcher 编译完成后
- ✅ 实测验证通过后
- ⏳ Phase 2 开始前

### 优先级
1. **功能 > 外观**
2. 先保证 Launcher 可用
3. 再美化 UI

### 实施步骤
1. 创建 `ui/components/` 包
2. 实现 `GlassPanel` 和 `GlassButton`
3. 逐步替换现有 UI 组件
4. 测试性能和兼容性

---

**注意**：Swing 毛玻璃效果有限，不要过度追求完美，功能优先！
