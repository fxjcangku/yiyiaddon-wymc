package com.yiyiaddon.wymc.runtime.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ModuleInfo - 模块元信息注解
 * 
 * 用法：
 * <pre>
 * @ModuleInfo(
 *     name = "HUD",
 *     description = "显示游戏信息",
 *     version = "1.0.0",
 *     category = Category.RENDER
 * )
 * public class HUDModule extends Module {
 *     // ...
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleInfo {
    
    /**
     * 模块名称
     */
    String name();
    
    /**
     * 模块描述
     */
    String description() default "";
    
    /**
     * 模块版本
     */
    String version() default "1.0.0";
    
    /**
     * 模块分类
     */
    Category category();
    
    /**
     * 默认启用
     */
    boolean enabledByDefault() default false;
}
