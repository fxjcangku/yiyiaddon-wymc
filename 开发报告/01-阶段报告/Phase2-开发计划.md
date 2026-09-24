# Phase 2 开发计划 - Mod Runtime 基础

**目标**: 建立 Mod 开发框架，实现游戏 API 访问和事件系统

---

## 📋 核心组件

### 1. MinecraftBridge
```
功能: 访问游戏 API
职责:
- 反射访问 Minecraft 类
- 处理混淆/非混淆差异
- 提供统一 API
- 缓存反射结果
```

### 2. EventBus
```
功能: 事件发布订阅
职责:
- 注册事件监听器
- 发布事件
- 异步/同步事件
- 优先级控制
```

### 3. ModuleManager
```
功能: 模块生命周期管理
职责:
- 加载/卸载 Module
- Module 启用/禁用
- Module 配置管理
- Module 间通信
```

### 4. Module 基类
```
功能: 可扩展功能模块
示例:
- HUDModule（信息显示）
- MiniMapModule（小地图）
- SchematicModule（建筑投影）
```

---

## 🏗️ 架构设计

```
┌─────────────────────────────────────┐
│         User Modules                │
│   (HUD, MiniMap, Schematic...)      │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│       ModuleManager                 │
│  (加载、启用、配置管理)              │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         EventBus                    │
│  (事件发布订阅、优先级)              │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      MinecraftBridge                │
│  (游戏 API 访问、反射封装)           │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│       Minecraft Runtime             │
│  (net.minecraft.* 或混淆类)         │
└─────────────────────────────────────┘
```

---

## 📦 模块划分

### wymc-runtime（新增）
```
wymc-runtime/
├── bridge/
│   ├── MinecraftBridge.java       # 主入口
│   ├── GameBridge.java            # 游戏状态访问
│   ├── PlayerBridge.java          # 玩家 API
│   ├── WorldBridge.java           # 世界 API
│   └── RenderBridge.java          # 渲染 API
├── event/
│   ├── EventBus.java              # 事件总线
│   ├── Event.java                 # 事件基类
│   ├── EventPriority.java         # 优先级枚举
│   └── events/                    # 具体事件
│       ├── TickEvent.java
│       ├── RenderEvent.java
│       └── KeyEvent.java
├── module/
│   ├── ModuleManager.java         # 模块管理器
│   ├── Module.java                # 模块基类
│   ├── ModuleInfo.java            # 模块元信息
│   └── Category.java              # 模块分类
└── util/
    ├── ReflectionUtil.java        # 反射工具
    └── MappingUtil.java           # Mapping 工具
```

---

## 🎯 Phase 2.1 - MinecraftBridge 设计

### 核心思路

#### 问题：混淆不确定
```
可能情况 1: 未混淆
  net.minecraft.client.Minecraft

可能情况 2: Mojang 混淆
  a.b.c

可能情况 3: 网易自定义混淆
  com.netease.mc.xxx
```

#### 解决方案：特征匹配 + Mapping
```java
// 1. 尝试直接访问
Class<?> clazz = Class.forName("net.minecraft.client.Minecraft");

// 2. 如果失败，通过特征匹配
clazz = findClassBySignature(
    "单例模式 + 持有 Window + 持有 World"
);

// 3. 建立 Mapping
mapping.put("Minecraft", clazz);
```

---

## 📅 开发时间线

### Week 1: 核心框架
```
Day 1-2: MinecraftBridge 接口设计和实现
Day 3-4: EventBus 实现
Day 5: ModuleManager 实现
Day 6-7: 测试和调试
```

### Week 2: Module 系统
```
Day 1-2: Module 基类和生命周期
Day 3-4: 第一个 Module（HUD）
Day 5-6: Agent 集成
Day 7: 测试和文档
```

---

## 🔧 技术选型

### 反射框架
```
选择: 原生 Java Reflection
原因: 
- 无额外依赖
- 性能足够
- 可控性强
```

### 事件系统
```
选择: 自实现（类似 EventBus）
原因:
- 轻量级
- 可定制
- 无依赖
```

### 配置系统
```
选择: JSON（Gson）
原因:
- 已有依赖
- 易于编辑
- 支持复杂结构
```

---

## ✅ 验收标准

### Phase 2.1 完成标志
```
✓ MinecraftBridge 可以访问游戏对象
✓ 支持混淆和非混淆环境
✓ 提供基础 API（Player、World、Game）
```

### Phase 2.2 完成标志
```
✓ EventBus 可以注册和触发事件
✓ 支持优先级和异步
✓ 提供基础事件（Tick、Render、Key）
```

### Phase 2.3 完成标志
```
✓ ModuleManager 可以加载 Module
✓ Module 可以启用/禁用
✓ 实现至少 1 个可用 Module（HUD）
```

---

**准备开始实现！**
