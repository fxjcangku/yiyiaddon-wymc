package com.yiyiaddon.wymc.launcher.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * 信息标签对
 * 用于显示 "键: 值" 格式的信息
 */
public class InfoLabel extends JPanel {
    
    private JLabel keyLabel;
    private JLabel valueLabel;
    
    public InfoLabel(String key, String defaultValue) {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 3));
        
        keyLabel = new JLabel(key);
        keyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        keyLabel.setForeground(new Color(120, 120, 120));
        
        valueLabel = new JLabel(defaultValue);
        valueLabel.setFont(new Font("Consolas", Font.BOLD, 13));
        valueLabel.setForeground(new Color(50, 50, 50));
        
        add(keyLabel);
        add(valueLabel);
    }
    
    public void setValue(String value) {
        valueLabel.setText(value);
    }
    
    public void setValueColor(Color color) {
        valueLabel.setForeground(color);
    }
    
    public String getValue() {
        return valueLabel.getText();
    }
}
