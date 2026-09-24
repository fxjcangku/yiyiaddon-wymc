package com.yiyiaddon.wymc.launcher.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * 分组面板
 * 带标题的信息分组容器
 */
public class SectionPanel extends JPanel {
    
    private JLabel titleLabel;
    private JPanel contentPanel;
    
    public SectionPanel(String title) {
        setOpaque(false);
        setLayout(new BorderLayout(5, 5));
        
        // 标题
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        titleLabel.setForeground(new Color(70, 70, 70));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 8, 5));
        
        // 内容区域
        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 2, 0, 0, new Color(0, 122, 255, 150)),
            BorderFactory.createEmptyBorder(0, 8, 0, 0)
        ));
        
        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    public void addInfo(InfoLabel infoLabel) {
        contentPanel.add(infoLabel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public void addInfo(String key, String value) {
        addInfo(new InfoLabel(key, value));
    }
    
    public void clearContent() {
        contentPanel.removeAll();
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
