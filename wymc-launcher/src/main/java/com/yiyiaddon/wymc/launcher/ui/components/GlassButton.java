package com.yiyiaddon.wymc.launcher.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 玻璃质感按钮
 * iOS 风格：圆角 + 渐变 + 悬停效果
 */
public class GlassButton extends JButton {
    
    private Color normalColor = new Color(0, 122, 255, 220);
    private Color hoverColor = new Color(30, 142, 255, 240);
    private Color pressedColor = new Color(0, 102, 235, 220);
    private Color disabledColor = new Color(200, 200, 200, 150);
    
    private Color currentColor;
    private boolean isHovered = false;
    private boolean isPressed = false;
    
    private int cornerRadius = 10;
    
    public GlassButton(String text) {
        super(text);
        currentColor = normalColor;
        
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        
        setFont(new Font("微软雅黑", Font.PLAIN, 13));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        setPreferredSize(new Dimension(128, 36));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    currentColor = hoverColor;
                    repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                currentColor = isEnabled() ? normalColor : disabledColor;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    currentColor = pressedColor;
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                currentColor = isHovered && isEnabled() ? hoverColor : normalColor;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // 按钮背景
        g2d.setColor(isEnabled() ? currentColor : disabledColor);
        g2d.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        
        // 渐变高光
        if (isHovered && !isPressed && isEnabled()) {
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(255, 255, 255, 50),
                0, height / 2, new Color(255, 255, 255, 0)
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, width, height / 2, cornerRadius, cornerRadius);
        }
        
        // 按下效果
        if (isPressed && isEnabled()) {
            g2d.setColor(new Color(0, 0, 0, 30));
            g2d.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        }
        
        g2d.dispose();
        super.paintComponent(g);
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        currentColor = enabled ? normalColor : disabledColor;
        setCursor(enabled ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
        repaint();
    }
    
    public void setButtonColor(Color normal, Color hover, Color pressed) {
        this.normalColor = normal;
        this.hoverColor = hover;
        this.pressedColor = pressed;
        this.currentColor = isEnabled() ? normal : disabledColor;
        repaint();
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
}
