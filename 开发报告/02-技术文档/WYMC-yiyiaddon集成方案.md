# WYMC + yiyiaddon 集成方案

**目标**: 在网易 Minecraft 中运行 yiyiaddon（无需修改 yiyiaddon 代码）

---

## 🏗️ 架构设计

```
网易 Minecraft 客户端
    ↓
WYMC Launcher 检测并注入 Agent
    ↓
WYMC Agent (wymc-agent.jar)
    ↓ 初始化
WYMC Runtime (MinecraftBridge + EventBus)
    ↓ 加载
Fabric 兼容层 (FakeFabricLoader + FabricEventsCompat)
    ↓ 加载
yiyiaddon.jar (原封不动，无需修改)
    ↓ 调用
YiyiAddonClient.onInitializeClient()
    ↓ 运行
所有 yiyiaddon 功能正常工作
```

---

## ✅ 已完成

### Phase 1 - Runtime 分析
- ✅ 进程检测
- ✅ 版本识别
- ✅ Runtime 分析
- ✅ Agent 注入基础

### Phase 2A - 核心 Runtime
- ✅ MinecraftBridge 框架
- ✅ EventBus 系统
- ✅ ModuleManager（WYMC 自己的）

### Phase 2B - Fabric 兼容层 ✨ **新增**
- ✅ FakeFabricLoader（伪装 Fabric Loader）
- ✅ FabricEventsCompat（Fabric 事件兼容）
- ✅ FakeModContainer（Mod 容器）
- ✅ YiyiAddonLoader（加载 yiyiaddon.jar）
- ✅ Agent 集成（支持加载参数）

---

## 🚀 使用方法

### 1. 编译项目

```powershell
cd yiyiaddon-wymc

# 编译 Agent
javac -d build/classes/agent wymc-agent/src/main/java/com/yiyiaddon/wymc/agent/*.java
jar cfm build/libs/wymc-agent.jar META-INF/MANIFEST.MF -C build/classes/agent .

# 编译 Runtime
javac -d build/classes/runtime wymc-runtime/src/main/java/com/yiyiaddon/wymc/runtime/**/*.java
jar cf build/libs/wymc-runtime.jar -C build/classes/runtime .
```

### 2. 启动网易 Minecraft

正常启动，进入游戏。

### 3. 运行 WYMC Launcher

```powershell
java -jar YiYi-WYMC-Loader.jar
```

### 4. 加载 yiyiaddon

Launcher 检测到 Minecraft 后，点击「加载 yiyiaddon」按钮。

**或者**手动附加：

```powershell
# 方式 1: 通过 Launcher GUI
点击「加载 Agent」→ 选择 yiyiaddon.jar

# 方式 2: 命令行
jattach <PID> load instrument false "load-yiyiaddon=D:/path/to/yiyiaddon.jar"
```

### 5. yiyiaddon 开始运行

- 打开 yiyiaddon GUI（如果有快捷键）
- 使用所有功能（自动挖矿、星露谷农场等）
- 在网易服务器中正常使用

---

## 🔧 工作原理

### Fabric API 伪装

#### 1. FabricLoader 伪装
```java
// yiyiaddon 调用
FabricLoader.getInstance().getGameDir()

// WYMC 提供
FakeFabricLoader.getInstance().getGameDir()
    → 返回 .minecraft 目录
```

#### 2. 事件系统桥接
```java
// yiyiaddon 注册
ClientTickEvents.END_CLIENT_TICK.register(client -> {
    // 每 tick 执行
});

// WYMC 转发
FabricEventsCompat 监听 WYMC TickEvent
    → 触发所有 Fabric 监听器
```

#### 3. Minecraft API 访问
```java
// yiyiaddon 调用
Minecraft.getInstance().player.getX()

// WYMC 提供
MinecraftBridge.getInstance().player().getX()
    → 通过反射访问真实玩家对象
```

---

## 📦 打包和分发

### 最终产物

```
YiYi-WYMC-Loader/
├── YiYi-WYMC-Loader.jar    # Launcher（带 GUI）
├── wymc-agent.jar          # Agent（注入到 MC）
├── wymc-runtime.jar        # Runtime（Fabric 兼容层）
└── README.md               # 使用说明
```

### 用户使用流程

1. 下载 WYMC Loader
2. 启动网易 Minecraft
3. 运行 Launcher
4. 选择 yiyiaddon.jar
5. 点击「加载」
6. 开始使用

**yiyiaddon 无需修改任何代码！**

---

## ⚠️ 当前限制和 TODO

### 已实现
- ✅ Fabric Loader 基础 API
- ✅ ClientTickEvents 兼容
- ✅ 加载 yiyiaddon.jar
- ✅ 调用初始化方法

### 待实现（按需添加）
- ⏳ MinecraftBridge 完整实现（需要实测网易 MC）
- ⏳ 更多 Fabric 事件（RenderEvent、KeyEvent 等）
- ⏳ Fabric API 其他部分（按 yiyiaddon 实际使用情况）
- ⏳ 配置文件读写（yiyiaddon 的配置）
- ⏳ GUI 集成（yiyiaddon 的 ClickGUI）

### 实测后完善
- 确认 yiyiaddon 具体使用了哪些 Fabric API
- 根据实际需求补充兼容层
- 处理网易 MC 的特殊情况（混淆等）

---

## 🎯 下一步

### 立即可做

**选项 1：实测验证**
1. 编译当前代码
2. 下载网易 MC
3. 测试注入
4. 尝试加载 yiyiaddon
5. 根据错误日志补充缺失的 API

**选项 2：继续完善 MinecraftBridge**
- 实现 PlayerBridge（坐标、血量等）
- 实现 WorldBridge（世界状态）
- 实现 RenderBridge（渲染）
- 准备好所有 yiyiaddon 可能用到的 API

**选项 3：分析 yiyiaddon 依赖**
- 详细扫描 yiyiaddon 使用了哪些 Fabric API
- 列出所有需要兼容的接口
- 优先实现高频使用的 API

---

## 💡 优势

1. **yiyiaddon 无需修改** - 原封不动加载
2. **模块化设计** - WYMC 可以单独使用
3. **可扩展** - 可以加载其他 Fabric Mod
4. **风险隔离** - Agent 出问题不影响游戏

---

**准备就绪！需要实测网易 MC 来验证和完善。**
