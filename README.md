<div align="center">

# YiYi WYMC Loader

<img src="https://img.shields.io/badge/Minecraft-网易我的世界-00A2E8?style=for-the-badge&logo=minecraft" alt="Minecraft">
<img src="https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
<img src="https://img.shields.io/badge/Platform-Windows-0078D6?style=for-the-badge&logo=windows" alt="Windows">
<img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License">

**网易 Minecraft 运行时分析与 Mod 加载工具**

[功能特性](#-功能特性) • [快速开始](#-快速开始) • [使用指南](#-使用指南) • [开发文档](#-开发文档) • [常见问题](#-常见问题)

---

<img src="docs/images/preview.png" alt="预览图" width="800">

</div>

---

## 📖 项目简介

YiYi WYMC Loader 是专为**网易 Minecraft Java 版**设计的运行时分析与 Mod 加载工具。

通过 Java Instrumentation 技术，在不修改游戏文件的情况下，实现：
- 🔍 **运行时分析** - 自动识别版本、分析混淆、探测 Runtime 结构
- 💉 **动态注入** - 基于 Java Agent 的标准注入方案
- 🎯 **智能检测** - 自动识别网易 MC 进程，不误检其他启动器
- 🎨 **现代 UI** - iOS 风格玻璃质感界面

### 💡 为什么选择 WYMC Loader？

| 特性 | WYMC Loader | 其他方案 |
|-----|------------|---------|
| **兼容性** | ✅ 专为网易 MC 设计 | ❌ 通用方案，兼容性差 |
| **安全性** | ✅ 标准 Java API | ⚠️ 可能需要修改游戏文件 |
| **易用性** | ✅ 一键检测注入 | ❌ 需要手动配置 |
| **可扩展** | ✅ 模块化架构 | ⚠️ 功能固定 |

---

## ✨ 功能特性

### 🎯 核心功能

#### 1️⃣ 智能进程检测
```
✅ 自动扫描 Java 进程
✅ 智能识别网易 Minecraft
✅ 排除 PCL/HMCL 等其他启动器
✅ 自动提取版本、路径、Java 信息
```

#### 2️⃣ Runtime 深度分析
```
✅ 封装类型识别 (Vanilla/Fabric/Forge/NetEase)
✅ 混淆检测 (Yarn/MCP/Mojang/Custom)
✅ ClassLoader 结构分析
✅ Runtime 转换探测 (ASM/Mixin/...)
```

#### 3️⃣ Java Agent 注入
```
✅ 标准 Attach API
✅ Instrumentation 能力检测
✅ ClassLoader 完整分析
✅ Minecraft 类探测
✅ 实时日志输出
```

#### 4️⃣ 完整报告生成
```
✅ EnvironmentReport.json - 环境信息
✅ RuntimeUnpackReport.json - 脱壳分析
✅ RuntimeDifferenceReport.json - 差异对比
✅ 详细日志记录
```

### 🎨 界面特色

<table>
<tr>
<td width="50%">
<h4>🪟 玻璃质感设计</h4>
<ul>
<li>半透明白色面板</li>
<li>圆角边框</li>
<li>iOS 风格按钮</li>
<li>渐变悬停效果</li>
</ul>
</td>
<td width="50%">
<h4>🎨 状态色彩反馈</h4>
<ul>
<li>🟢 成功 - 绿色</li>
<li>🟠 等待 - 橙色</li>
<li>🔴 错误 - 红色</li>
<li>🔵 信息 - 蓝色</li>
</ul>
</td>
</tr>
</table>

---

## 🚀 快速开始

### 📋 系统要求

- **操作系统**: Windows 10/11
- **Java 版本**: JDK 17 或更高
- **网易 Minecraft**: 已安装并可正常运行
- **内存**: 建议 4GB+

### 📥 下载安装

#### 方式 1：下载 Release（推荐）

1. 前往 [Releases](https://github.com/你的用户名/yiyiaddon-wymc/releases) 页面
2. 下载最新版本的 `YiYi-WYMC-Loader.jar`
3. 双击运行或使用命令：
   ```bash
   java -jar YiYi-WYMC-Loader.jar
   ```

#### 方式 2：从源码编译

```bash
# 克隆仓库
git clone https://github.com/你的用户名/yiyiaddon-wymc.git
cd yiyiaddon-wymc

# 下载依赖（Windows PowerShell）
cd build/libs/deps
# 运行依赖下载脚本（见文档）

# 编译
javac --release 17 -encoding UTF-8 -cp "build/libs/deps/*" ...
# 详细步骤见 网易MC安装和测试指南.md
```

---

## 📚 使用指南

### 🎮 基础使用

#### 步骤 1：启动网易 Minecraft
```
1. 打开网易 MC 启动器
2. 登录账号
3. 启动游戏
4. 进入主菜单或单人世界
```

#### 步骤 2：运行 WYMC Loader
```bash
java -jar YiYi-WYMC-Loader.jar
```

#### 步骤 3：自动检测
- Loader 会自动扫描并识别网易 MC 进程
- 显示 PID、版本、Java 信息

#### 步骤 4：分析 Runtime
点击 **"分析 Runtime"** 按钮：
- 识别封装类型
- 检测混淆方式
- 分析 ClassLoader

#### 步骤 5：加载 Agent
点击 **"加载 Agent"** 按钮：
- 注入分析工具
- 查看控制台输出
- 获取详细 Runtime 信息

#### 步骤 6：导出报告
点击 **"导出报告"** 按钮：
- 生成完整分析报告
- 保存到 `~/.yiyiaddon-wymc/reports/`

### 📊 界面说明

```
┌─────────────────────────────────────┐
│  YiYi WYMC Loader v1.0.0           │
├─────────────────────────────────────┤
│ [Minecraft]                         │
│  状态: DETECTED ✅                  │
│  PID: 12345                         │
│  版本: 1.20.1                       │
│                                     │
│ [Java]                              │
│  Version: 17.0.8                    │
│  VM: HotSpot                        │
│                                     │
│ [Runtime]                           │
│  NetEase: NETEASE_CUSTOM            │
│  Mapping: CUSTOM_OBFUSCATED         │
│  ClassLoader: CUSTOM                │
│                                     │
│ [Injection]                         │
│  Attach: SUCCESS ✅                 │
│  Agent: LOADED ✅                   │
├─────────────────────────────────────┤
│ [重新检测] [分析Runtime] [加载Agent]│
│ [导出报告] [打开日志]               │
└─────────────────────────────────────┘
```

---

## 🛠️ 开发文档

### 📁 项目结构

```
yiyiaddon-wymc/
├── wymc-launcher/          # 启动器（独立进程）
│   ├── ui/                 # 玻璃质感 UI
│   ├── process/            # 进程检测
│   ├── environment/        # 环境分析
│   ├── analysis/           # Runtime 分析
│   ├── attach/             # Agent 附加
│   └── report/             # 报告生成
├── wymc-agent/             # Java Agent（注入到 MC）
│   └── analysis/           # Runtime 深度分析
├── 开发报告/               # 完整技术文档
│   ├── 01-阶段报告/
│   ├── 02-技术文档/
│   └── 03-构建日志/
└── docs/                   # 用户文档
```

### 🏗️ 技术栈

- **语言**: Java 17
- **GUI**: Swing (玻璃质感组件)
- **注入**: Java Attach API + Instrumentation
- **进程**: JNA (Windows API)
- **日志**: SLF4J + Logback
- **序列化**: Gson

### 📖 详细文档

- [技术架构文档](开发报告/02-技术文档/技术架构文档.md)
- [UI 实现指南](开发报告/02-技术文档/玻璃质感UI实现指南.md)
- [Phase 1 完成报告](开发报告/01-阶段报告/20260924-Phase1-完成报告.md)
- [开发进展报告](开发报告/01-阶段报告/开发进展报告-20260924.md)

### 🔧 参与开发

```bash
# Fork 项目
git clone https://github.com/你的用户名/yiyiaddon-wymc.git

# 创建分支
git checkout -b feature/your-feature

# 提交更改
git commit -m "feat: add your feature"

# 推送分支
git push origin feature/your-feature

# 创建 Pull Request
```

---

## ❓ 常见问题

<details>
<summary><b>Q: 为什么找不到 Minecraft 进程？</b></summary>

**A:** 可能的原因：
1. 网易 MC 未启动或未完全加载
2. 使用了其他启动器（PCL/HMCL）- 本工具只支持网易官方启动器
3. 进程检测算法需要调整

**解决方法**：
- 确认使用网易官方启动器
- 等待游戏完全加载后点击"重新检测"
- 查看日志 `~/.yiyiaddon-wymc/logs/wymc-loader.log`
</details>

<details>
<summary><b>Q: Agent 加载失败怎么办？</b></summary>

**A:** 可能的原因：
1. Java 版本不兼容（需要 JDK 17+）
2. 权限不足
3. Agent JAR 文件损坏

**解决方法**：
- 检查 Java 版本：`java -version`
- 以管理员权限运行
- 重新下载或编译 Agent JAR
</details>

<details>
<summary><b>Q: 支持 Fabric/Forge 吗？</b></summary>

**A:** 
- **检测支持**: 可以识别 Fabric/Forge 环境
- **Mod 加载**: Phase 1 仅支持分析，Phase 2 将支持 Mod 加载
- **兼容性**: 设计目标是网易原版，Fabric/Forge 环境需要额外适配
</details>

<details>
<summary><b>Q: 会被检测/封号吗？</b></summary>

**A:** 
- 本工具使用标准 Java API
- Phase 1 仅进行**只读分析**，不修改游戏逻辑
- 不干扰游戏正常运行
- **但仍建议谨慎使用，自行承担风险**
</details>

---

## 🗺️ 开发路线

### ✅ Phase 1 - 基础框架（已完成）
- [x] Minecraft 进程检测
- [x] 版本自动识别
- [x] Runtime 分析器
- [x] Java Agent 注入
- [x] 玻璃质感 UI
- [x] 完整报告系统

### 🚧 Phase 2 - Mod Runtime（开发中）
- [ ] MinecraftBridge（游戏 API 桥接）
- [ ] EventBus（事件系统）
- [ ] ModuleManager（模块管理）
- [ ] 测试 Mod（HUD 显示）

### 📅 Phase 3 - Mod 开发（计划中）
- [ ] 建筑投影
- [ ] Printer / 建筑辅助
- [ ] 背包整理
- [ ] 自动寻路
- [ ] Fabric Mod 兼容

---

## 🤝 贡献指南

欢迎贡献代码、报告 Bug 或提出建议！

### 贡献方式
1. 🐛 [报告 Bug](https://github.com/你的用户名/yiyiaddon-wymc/issues/new?template=bug_report.md)
2. 💡 [功能建议](https://github.com/你的用户名/yiyiaddon-wymc/issues/new?template=feature_request.md)
3. 📖 改进文档
4. 🔧 提交代码

### 代码规范
- 遵循 Google Java Style Guide
- 代码注释使用中文
- Commit 信息使用 Conventional Commits

---

## 📜 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

```
MIT License

Copyright (c) 2024 YiYi Addon Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction...
```

---

## 🙏 致谢

- **Minecraft** - Mojang Studios
- **网易 Minecraft** - NetEase Games
- **Fabric** - 参考了 Mod Loader 设计
- **社区贡献者** - 感谢所有提供反馈和建议的用户

---

## 📞 联系方式

- **Issue**: [GitHub Issues](https://github.com/你的用户名/yiyiaddon-wymc/issues)
- **讨论**: [GitHub Discussions](https://github.com/你的用户名/yiyiaddon-wymc/discussions)
- **邮箱**: your-email@example.com

---

<div align="center">

**如果这个项目对你有帮助，请给个 ⭐ Star！**

Made with ❤️ by YiYi Addon Team

</div>
