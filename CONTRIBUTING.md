# 贡献指南

感谢你对 YiYi WYMC Loader 的关注！

---

## 🐛 报告 Bug

发现问题？请通过 [GitHub Issues](https://github.com/你的用户名/yiyiaddon-wymc/issues/new) 报告。

### Bug 报告模板

```markdown
**描述问题**
清晰简洁地描述 Bug。

**复现步骤**
1. 启动 '...'
2. 点击 '...'
3. 滚动到 '...'
4. 看到错误

**预期行为**
描述你期望发生什么。

**截图**
如果可能，添加截图帮助解释问题。

**环境信息**
 - 操作系统: [e.g. Windows 11]
 - Java 版本: [e.g. OpenJDK 17.0.8]
 - Minecraft 版本: [e.g. 1.20.1]
 - WYMC Loader 版本: [e.g. 1.0.0]

**日志**
粘贴 `~/.yiyiaddon-wymc/logs/wymc-loader.log` 相关部分。
```

---

## 💡 功能建议

有好想法？通过 [GitHub Issues](https://github.com/你的用户名/yiyiaddon-wymc/issues/new) 告诉我们。

### 功能建议模板

```markdown
**功能描述**
清晰简洁地描述你想要的功能。

**使用场景**
描述这个功能的使用场景。
例如：当我 [...] 时，我希望 [...]。

**替代方案**
描述你考虑过的替代方案。

**其他信息**
添加任何其他相关信息或截图。
```

---

## 🔧 提交代码

### 开发环境设置

```bash
# 1. Fork 并克隆仓库
git clone https://github.com/你的用户名/yiyiaddon-wymc.git
cd yiyiaddon-wymc

# 2. 创建开发分支
git checkout -b feature/your-feature-name

# 3. 编译项目
# 见 网易MC安装和测试指南.md
```

### 代码规范

- **语言**: Java 17
- **风格**: Google Java Style Guide
- **注释**: 使用中文
- **格式**: 4 空格缩进

### Commit 信息规范

使用 [Conventional Commits](https://www.conventionalcommits.org/zh-hans/)：

```
<类型>(<范围>): <描述>

[可选的正文]

[可选的脚注]
```

**类型**:
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式（不影响功能）
- `refactor`: 重构
- `test`: 测试
- `chore`: 构建/工具

**示例**:
```
feat(ui): 添加玻璃质感按钮组件

- 实现 GlassButton 类
- 支持三种状态（normal/hover/pressed）
- 添加渐变悬停效果
```

### Pull Request 流程

1. **确保代码质量**
   - 编译无错误
   - 代码符合规范
   - 添加必要注释

2. **测试你的更改**
   - 本地测试通过
   - 不引入新 Bug

3. **更新文档**
   - 修改相关 README
   - 更新 CHANGELOG.md

4. **创建 Pull Request**
   - 清晰的标题
   - 详细的描述
   - 引用相关 Issue

---

## 📝 文档贡献

文档同样重要！

### 文档类型
- **用户文档**: README.md, 使用指南
- **开发文档**: 技术架构、API 文档
- **注释**: 代码内注释

### 文档规范
- 使用 Markdown 格式
- 语言：中文
- 图片：存放在 `docs/images/`

---

## ✅ 代码审查标准

PR 将根据以下标准审查：

### 功能
- [ ] 功能完整，符合需求
- [ ] 无明显 Bug
- [ ] 不破坏现有功能

### 代码质量
- [ ] 代码清晰易读
- [ ] 注释充分
- [ ] 无冗余代码

### 规范
- [ ] 符合代码风格
- [ ] Commit 信息规范
- [ ] 文档完整

---

## 🙏 感谢

感谢每一位贡献者！

你的名字将出现在：
- README.md 致谢章节
- CHANGELOG.md 版本记录
- Contributors 页面

---

## ❓ 需要帮助？

- 阅读 [开发文档](开发报告/02-技术文档/)
- 查看 [常见问题](README.md#-常见问题)
- 在 [Discussions](https://github.com/你的用户名/yiyiaddon-wymc/discussions) 提问
