package com.yiyiaddon.wymc.runtime.module;

/**
 * Category - 模块分类
 * 
 * 用于对模块进行分类管理
 */
public enum Category {
    
    /**
     * 渲染类
     * 例如：HUD、小地图、ESP
     */
    RENDER("渲染"),
    
    /**
     * 移动类
     * 例如：飞行、加速、自动跳跃
     */
    MOVEMENT("移动"),
    
    /**
     * 战斗类
     * 例如：自动攻击、暴击
     */
    COMBAT("战斗"),
    
    /**
     * 玩家类
     * 例如：自动吃食物、自动修复
     */
    PLAYER("玩家"),
    
    /**
     * 世界类
     * 例如：透视、路径寻找
     */
    WORLD("世界"),
    
    /**
     * 建筑类
     * 例如：投影、Printer
     */
    BUILD("建筑"),
    
    /**
     * 工具类
     * 例如：自动整理、自动钓鱼
     */
    MISC("工具"),
    
    /**
     * 客户端类
     * 例如：GUI、设置
     */
    CLIENT("客户端");
    
    private final String displayName;
    
    Category(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
