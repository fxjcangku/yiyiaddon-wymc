# 截图说明

由于 Launcher 需要实际运行才能截图，这里提供占位说明。

## 需要的截图

### 1. 主界面截图
- **文件名**: `ui-main.png`
- **内容**: 启动后的主界面（等待检测状态）
- **尺寸**: 750x850

### 2. 检测中状态
- **文件名**: `ui-detecting.png`
- **内容**: 点击"重新检测"时的状态
- **尺寸**: 750x850

### 3. 检测成功
- **文件名**: `ui-success.png`
- **内容**: 检测到 Minecraft 后的界面
- **尺寸**: 750x850
- **显示**: 
  - 状态: DETECTED (绿色)
  - PID: 显示真实 PID
  - 版本: 显示识别版本

### 4. Agent 已加载
- **文件名**: `ui-agent-loaded.png`
- **内容**: Agent 加载成功后的界面
- **尺寸**: 750x850
- **显示**:
  - Attach: SUCCESS (绿色)
  - Agent: LOADED (绿色)

### 5. 演示 GIF
- **文件名**: `demo.gif`
- **内容**: 完整操作流程
- **尺寸**: 800x600
- **流程**:
  1. 启动 Launcher
  2. 自动检测进程
  3. 点击"分析 Runtime"
  4. 点击"加载 Agent"
  5. 显示成功状态

### 6. 功能演示
- **ui-showcase.png** - 界面展示
- **runtime-analysis.png** - Runtime 分析结果
- **report-output.png** - 报告输出目录

## 如何截图

### 方法 1：运行 Launcher 截图
```powershell
# 1. 启动 Launcher
java -jar build/libs/YiYi-WYMC-Loader.jar

# 2. 使用 Windows 截图工具
Win + Shift + S

# 3. 保存到 docs/images/
```

### 方法 2：使用占位图
临时使用占位图，等实际运行后替换：
- https://via.placeholder.com/750x850/F0F2F5/007AFF?text=UI+Screenshot

### 方法 3：使用工具生成
使用 ScreenToGif 录制操作流程并导出 GIF。

## 临时方案

README 中已配置占位图路径，可以先推送，后续替换真实截图。

**优先级**:
1. **logo.svg** ✅ 已生成
2. **demo.gif** ⏳ 需要实际运行
3. **ui-*.png** ⏳ 需要实际运行
4. 其他装饰图 ⏳ 可选
