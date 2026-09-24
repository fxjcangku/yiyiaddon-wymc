package com.yiyiaddon.wymc.launcher.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * 玻璃质感面板
 * 模拟 iOS 毛玻璃效果：半透明白色背景 + 圆角 + 阴影
 */
public class GlassPanel extends JPanel {
    
    private Color backgroundColor = new Color(255, 255, 255, 230);
    private Color borderColor = new Color(200, 200, 200, 120);
    private int cornerRadius = 15;
    private boolean showShadow = true;
    
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
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // 阴影效果
        if (showShadow) {
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.fillRoundRect(3, 3, width - 3, height - 3, cornerRadius, cornerRadius);
            g2d.setColor(new Color(0, 0, 0, 10));
            g2d.fillRoundRect(2, 2, width - 2, height - 2, cornerRadius, cornerRadius);
        }
        
        // 玻璃背景
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, width - 3, height - 3, cornerRadius, cornerRadius);
        
        // 高光效果（顶部渐变）
        GradientPaint highlight = new GradientPaint(
            0, 0, new Color(255, 255, 255, 80),
            0, height / 3, new Color(255, 255, 255, 0)
        );
        g2d.setPaint(highlight);
        g2d.fillRoundRect(0, 0, width - 3, height / 3, cornerRadius, cornerRadius);
        
        // 边框
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(1.2f));
        g2d.drawRoundRect(0, 0, width - 4, height - 4, cornerRadius, cornerRadius);
        
        g2d.dispose();
        super.paintComponent(g);
    }
    
    public void setGlassColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    public void setShadowEnabled(boolean enabled) {
        this.showShadow = enabled;
        repaint();
    }
}
