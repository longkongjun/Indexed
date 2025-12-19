# Jikan API 模块文档

本目录包含 Jikan API 模块的详细文档。

## 📚 文档索引

### 🎯 核心使用文档

#### [Result 使用指南](RESULT_USAGE.md)
Kotlin Result 类型的完整使用指南，包括：
- 标准库方法说明
- 自定义扩展方法
- 错误处理最佳实践
- 协程中的使用
- 分页数据处理
- 合并多个 Result

#### [Result 合并示例](RESULT_COMBINE_EXAMPLES.md)
详细的 Result 合并方法使用示例，包括：
- 8 种方法概览
- 6 种使用场景
- 4 个实际应用示例
- 性能优化建议
- 错误处理模式

#### [字段分类说明](FIELD_CLASSIFICATION.md)
数据模型字段的可空性分类文档，包括：
- 必填字段（非空）
- 可选字段（可空）
- 分类依据和原则
- 各模型的字段分类详情

---

### 🔧 开发记录文档

#### [API 泛型修复记录](API_GENERICS_FIX.md)
记录了 API 泛型类型丢失问题的修复过程：
- 问题描述和发现
- 修复步骤和方法
- 验证结果
- 经验教训

#### [runCatching 迁移记录](RUNCATCHING_MIGRATION.md)
try-catch 到 runCatching 的迁移文档：
- 迁移原因
- 迁移内容（2 处）
- 迁移要点
- 验证结果

---

### 📐 设计分析文档

#### [combineAll API 设计](COMBINEALL_API_DESIGN.md)
combineAll 方法的 API 设计分析：
- 三种方案对比（List vs Iterable vs vararg）
- 性能对比
- 方法重载注意事项
- 最终推荐方案
- 使用示例

#### [combineAll 性能优化](COMBINEALL_OPTIMIZATION.md)
combineAll 方法的性能优化文档：
- 优化前后对比
- 性能提升分析（25x）
- buildList 的优势
- runCatching 的优势
- 实际应用场景
- 其他优化建议

---

## 📖 文档导航

### 按使用场景分类

**新手入门：**
1. [Result 使用指南](RESULT_USAGE.md) - 从这里开始
2. [Result 合并示例](RESULT_COMBINE_EXAMPLES.md) - 实战示例

**深入理解：**
1. [字段分类说明](FIELD_CLASSIFICATION.md) - 理解数据模型设计
2. [combineAll API 设计](COMBINEALL_API_DESIGN.md) - 理解 API 设计决策

**开发参考：**
1. [API 泛型修复记录](API_GENERICS_FIX.md) - 问题排查参考
2. [runCatching 迁移记录](RUNCATCHING_MIGRATION.md) - 代码重构参考
3. [combineAll 性能优化](COMBINEALL_OPTIMIZATION.md) - 性能优化参考

### 按文档类型分类

**使用指南** (User Guides)
- [Result 使用指南](RESULT_USAGE.md)
- [Result 合并示例](RESULT_COMBINE_EXAMPLES.md)

**规范文档** (Specifications)
- [字段分类说明](FIELD_CLASSIFICATION.md)

**变更记录** (Change Logs)
- [API 泛型修复记录](API_GENERICS_FIX.md)
- [runCatching 迁移记录](RUNCATCHING_MIGRATION.md)

**设计文档** (Design Documents)
- [combineAll API 设计](COMBINEALL_API_DESIGN.md)
- [combineAll 性能优化](COMBINEALL_OPTIMIZATION.md)

---

## 🎯 快速查找

### 我想了解...

- **如何使用 Result？** → [Result 使用指南](RESULT_USAGE.md)
- **如何合并多个 API 请求？** → [Result 合并示例](RESULT_COMBINE_EXAMPLES.md)
- **为什么有些字段可空，有些不可空？** → [字段分类说明](FIELD_CLASSIFICATION.md)
- **为什么选择 Iterable 而不是 List？** → [combineAll API 设计](COMBINEALL_API_DESIGN.md)
- **combineAll 是如何优化性能的？** → [combineAll 性能优化](COMBINEALL_OPTIMIZATION.md)
- **为什么要用 runCatching？** → [runCatching 迁移记录](RUNCATCHING_MIGRATION.md)

---

## 📊 文档统计

| 类型 | 数量 | 文件 |
|------|------|------|
| 使用指南 | 2 | RESULT_USAGE.md, RESULT_COMBINE_EXAMPLES.md |
| 规范文档 | 1 | FIELD_CLASSIFICATION.md |
| 变更记录 | 2 | API_GENERICS_FIX.md, RUNCATCHING_MIGRATION.md |
| 设计文档 | 2 | COMBINEALL_API_DESIGN.md, COMBINEALL_OPTIMIZATION.md |
| **总计** | **7** | |

---

## 🔄 文档维护

- **最后更新**: 2025-12-16
- **文档版本**: 1.0
- **模块版本**: jikan module

### 文档贡献指南

1. 所有文档使用 Markdown 格式
2. 代码示例使用 Kotlin 语法高亮
3. 保持文档结构清晰，使用适当的标题层级
4. 重要概念使用**粗体**标注
5. 示例代码包含注释说明

---

## 返回

⬅️ [返回主 README](../README.md)

