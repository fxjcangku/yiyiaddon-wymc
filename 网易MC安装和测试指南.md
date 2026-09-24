# 网易 Minecraft 安装和测试指南

**目标**: 下载安装网易 Minecraft，完成 YiYi WYMC Loader 首次实测

---

## 一、下载网易 Minecraft

### 官方下载地址
https://mc.163.com/

### 安装步骤
1. 访问官网
2. 点击"立即下载"
3. 下载 PC 版启动器
4. **安装到 D 盘**（推荐 `D:\Games\NetEase Minecraft\`）

### 系统要求
- Windows 10/11
- Java 17+ (已安装)
- 4GB+ 内存
- 网易账号

---

## 二、当前编译状态

### ✅ 已完成
- Agent JAR: `build/libs/wymc-agent.jar` (8KB)

### ⏳ 待完成
- Launcher JAR（需要下载依赖）

---

## 三、完整编译步骤（PowerShell）

```powershell
# 进入项目目录
cd D:\mcaddon\yiyiaddon-wymc

# 1. 创建目录结构
New-Item -ItemType Directory -Path build/libs/deps -Force

# 2. 下载依赖（大约 3MB）
cd build/libs/deps

Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar" -OutFile "gson-2.10.1.jar"
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar" -OutFile "slf4j-api-2.0.9.jar"
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/ch/qos/logback/logback-classic/1.4.11/logback-classic-1.4.11.jar" -OutFile "logback-classic-1.4.11.jar"
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.4.11/logback-core-1.4.11.jar" -OutFile "logback-core-1.4.11.jar"
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/net/java/dev/jna/jna/5.13.0/jna-5.13.0.jar" -OutFile "jna-5.13.0.jar"
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/net/java/dev/jna/jna-platform/5.13.0/jna-platform-5.13.0.jar" -OutFile "jna-platform-5.13.0.jar"

cd ../../..

# 3. 编译 Launcher
New-Item -ItemType Directory -Path build/classes/launcher -Force

javac --release 17 -encoding UTF-8 -cp "build/libs/deps/*" -d build/classes/launcher `
  wymc-launcher/src/main/java/com/yiyiaddon/wymc/launcher/WymcLauncher.java `
  wymc-launcher/src/main/java/com/yiyiaddon/wymc/launcher/ui/MainWindow.java `
  wymc-launcher/src/main/java/com/yiyiaddon/wymc/launcher/process/MinecraftProcessDetector.java `
  wymc-launcher/src/main/java/com/yiyiaddon/wymc/launcher/process/ProcessCandidate.java `
  wymc-launcher/src/main/java/com/yiyiaddon/wymc/launcher/environment/MinecraftVersionDetector.java `
  wymc-launcher/src/main/java/com/yiyiaddon/wymc/launcher/attach/AgentAttacher.java `
  wymc-launcher/src/main/java/com/yiyiaddon/wymc/launcher/attach/AttachResult.java `
  wymc-launcher/src/main/java/com/yiyiaddon/wymc/launcher/analysis/runtime/RuntimeAnalyzer.java `
  wymc-launcher/src/main/java/com/yiyiaddon/wymc/launcher/report/ReportGenerator.java

# 4. 打包 Launcher
cd build/classes/launcher

# 复制资源文件
Copy-Item ../../../wymc-launcher/src/main/resources/* . -Recurse -Force

# 解压所有依赖
jar xf ../../libs/deps/gson-2.10.1.jar
jar xf ../../libs/deps/slf4j-api-2.0.9.jar
jar xf ../../libs/deps/logback-classic-1.4.11.jar
jar xf ../../libs/deps/logback-core-1.4.11.jar
jar xf ../../libs/deps/jna-5.13.0.jar
jar xf ../../libs/deps/jna-platform-5.13.0.jar

# 删除签名文件
Remove-Item META-INF/*.SF -ErrorAction SilentlyContinue
Remove-Item META-INF/*.DSA -ErrorAction SilentlyContinue
Remove-Item META-INF/*.RSA -ErrorAction SilentlyContinue

# 打包成 JAR
jar cfe ../../libs/YiYi-WYMC-Loader.jar com.yiyiaddon.wymc.launcher.WymcLauncher .

cd ../../..

Write-Host "编译完成！" -ForegroundColor Green
Write-Host "Agent: build/libs/wymc-agent.jar" -ForegroundColor Cyan
Write-Host "Launcher: build/libs/YiYi-WYMC-Loader.jar" -ForegroundColor Cyan
```

---

## 四、测试流程

### 前提条件
- ✅ 网易 Minecraft 已安装
- ✅ Launcher JAR 已编译
- ✅ Agent JAR 已编译

### 测试步骤

#### 1. 启动网易 Minecraft
```
- 打开网易 MC 启动器
- 登录账号
- 启动游戏
- 等待完全进入主菜单或单人世界
```

#### 2. 运行 WYMC Loader
```powershell
cd D:\mcaddon\yiyiaddon-wymc
java -jar build/libs/YiYi-WYMC-Loader.jar
```

#### 3. 观察 GUI
- 是否显示 "DETECTED"？
- PID 是否正确？
- 版本是否识别？

#### 4. 点击"分析 Runtime"
查看：
- Packaging Type (VANILLA/FABRIC/NETEASE_CUSTOM)
- Obfuscation Type (NONE/CUSTOM_OBFUSCATED)
- ClassLoader Type

#### 5. 点击"加载 Agent"
**关键时刻！观察控制台输出**：
```
期望输出:
[WYMC] Agent attached successfully
[WYMC] Instrumentation initialized
[WYMC] Runtime loaded successfully
[WYMC] Found X ClassLoaders
[WYMC] Minecraft classes: XXX
[WYMC] NetEase classes: XXX
```

#### 6. 查看报告
```powershell
# 打开报告目录
explorer $env:USERPROFILE\.yiyiaddon-wymc\reports

# 查看日志
notepad $env:USERPROFILE\.yiyiaddon-wymc\logs\wymc-loader.log
```

---

## 五、关键检查点

### 检查 1: 类名是否混淆？

**查看方法**: 
- 打开 `EnvironmentReport.json`
- 或查看控制台的 "Sample Minecraft classes"

**如果输出**：
```
net.minecraft.client.Minecraft
net.minecraft.client.player.LocalPlayer
```
→ **未混淆**，后续开发简单

**如果输出**：
```
a.b.c.d
e.f.g.h
```
→ **已混淆**，需要建立 Mapping

### 检查 2: ClassLoader 类型？

**期望**: `AppClassLoader` 或 自定义

**影响**: 决定 MinecraftBridge 如何访问类

### 检查 3: Instrumentation 能力

**查看**:
- Can Retransform: true/false
- Can Redefine: true/false

**影响**: 决定是否可以修改字节码

### 检查 4: 有无反调试？

**症状**:
- Attach 失败
- Agent 加载后游戏崩溃
- 无法获取 ClassLoader

**如果出现**: 需要分析反调试机制

---

## 六、可能的问题

### 问题 1: 找不到进程
**原因**: 进程特征匹配失败  
**解决**: 查看日志，调整评分算法

### 问题 2: Attach 失败
**原因**: 权限不足 或 JVM 版本不兼容  
**解决**: 
- 以管理员运行
- 检查 JDK 版本

### 问题 3: Agent 加载失败
**原因**: Manifest 错误 或 类加载失败  
**解决**: 
- 检查 wymc-agent.jar 完整性
- 查看详细错误日志

### 问题 4: 游戏崩溃
**原因**: 可能有反调试机制  
**解决**: 
- 记录崩溃前的日志
- 分析是否有完整性检查

---

## 七、测试报告模板

测试完成后，填写以下信息：

```markdown
## 网易 Minecraft 实测报告

**测试时间**: 2026-XX-XX
**Minecraft 版本**: [从 GUI 或报告获取]
**Java 版本**: [从 GUI 获取]

### 1. 进程检测
- [ ] 成功检测
- [ ] PID: _____
- [ ] 评分: _____

### 2. 版本识别
- [ ] 成功识别
- [ ] 版本: _____
- [ ] 置信度: _____

### 3. Runtime 分析
- Packaging Type: _____
- Obfuscation Type: _____
- ClassLoader: _____

### 4. Agent 注入
- [ ] 注入成功
- [ ] Instrumentation 可用
- [ ] Can Retransform: _____

### 5. 类名情况
**样例类名**:
```
[粘贴控制台输出的示例类名]
```

**结论**: 
- [ ] 未混淆（可直接使用）
- [ ] 已混淆（需要 Mapping）

### 6. 遇到的问题
[描述任何问题]

### 7. 下一步建议
[根据测试结果提出建议]
```

---

## 八、下一步方向

### 情况 A: 一切正常，类名未混淆
**工作量**: 1-2 天  
**任务**:
1. 建立 MinecraftBridge
2. 实现 EventBus
3. 开发测试 Mod

### 情况 B: 类名已混淆
**工作量**: 3-5 天  
**任务**:
1. 实现 Class Dump
2. 建立 Mapping 系统
3. 然后继续 A 的任务

### 情况 C: 有反调试
**工作量**: 2-4 天  
**任务**:
1. 分析反调试机制
2. 实现绕过方案
3. 重新测试

---

**准备工作**: 我已完成所有代码，等你安装网易 MC 后即可测试！
