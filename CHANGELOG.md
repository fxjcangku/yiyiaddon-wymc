# 更新日志

所有重要的项目变更都会记录在此文件中。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

---

## [1.0.0-PHASE1] - 2024-09-24

### 🎉 首次发布

这是 YiYi WYMC Loader 的第一个公开版本！

### ✨ 新增功能

#### 核心功能
- **智能进程检测** - 自动识别网易 Minecraft，排除其他启动器
- **版本自动识别** - 多来源识别（命令行/Classpath/工作目录）
- **Runtime 分析器** - 封装类型、混淆识别、ClassLoader 分析
- **Java Agent 注入** - 标准 Attach API 实现
- **ClassLoader 分析** - 完整结构探测
- **Minecraft 类探测** - 运行时类识别

#### UI 界面
- **玻璃质感设计** - iOS 风格半透明面板
- **现代按钮组件** - 渐变悬停效果
- **状态色彩反馈** - 绿/橙/红状态指示
- **信息分组展示** - 清晰的信息层次

#### 报告系统
- **环境报告** (EnvironmentReport.json)
- **Runtime 脱壳报告** (RuntimeUnpackReport.json)
- **差异对比报告** (RuntimeDifferenceReport.json)
- **完整日志** (wymc-loader.log)

### 📦 构建产物
- `wymc-agent.jar` (8KB) - Java Agent
- `YiYi-WYMC-Loader.jar` (5MB) - 启动器

### 📚 文档
- 完整的开发报告（7 个文档）
- 技术架构文档
- UI 实现指南
- 网易 MC 测试指南

### 🔧 技术实现
- Java 17 兼容
- Swing GUI（玻璃质感组件）
- JNA 进程检测
- SLF4J + Logback 日志
- Gson JSON 序列化

---

## [待发布] - Phase 2

### 计划功能
- [ ] MinecraftBridge（游戏 API 桥接）
- [ ] EventBus（事件系统）
- [ ] ModuleManager（模块管理）
- [ ] 测试 Mod（HUD 显示）

---

## [待发布] - Phase 3

### 计划功能
- [ ] 建筑投影
- [ ] Printer / 建筑辅助
- [ ] 背包整理
- [ ] 自动寻路
- [ ] Fabric Mod 兼容
