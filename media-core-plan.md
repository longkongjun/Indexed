## MediaFlow 媒体核心服务方案（Rust）

### 1. 概要
- 角色：负责高性能文件系统操作与媒体基础能力，供上游（Go 主服务）调用。
- 当前能力（MVP）：健康检查、目录扫描（递归可选，按扩展名/最小大小过滤）。
- 部署：独立进程，默认监听 `0.0.0.0:8088`。

### 2. 依赖
- 运行环境：Rust 1.75+（cargo 兼容）；可容器化。
- 系统工具：无强依赖（后续如需 `ffprobe`/`mediainfo` 再补充）。
- 第三方 crate：`axum`, `tokio`, `serde`, `tracing`, `time`, `walkdir`, `thiserror` 等。

### 3. 结构
- `src/main.rs`：服务入口，初始化日志与路由。
- `src/api/`：HTTP 路由与请求/响应模型
  - `health.rs`：`GET /health`
  - `scan.rs`：`POST /internal/scan`
- `src/domain/`：领域模型
  - `file_entry.rs`：扫描结果的条目结构
- `src/services/`：业务实现
  - `scanner.rs`：目录扫描（spawn_blocking 包裹同步遍历）
- `src/error.rs`：统一错误类型与 HTTP 映射

### 4. 接口说明
#### 4.1 `GET /health`
- 输入：无
- 输出：
  - `status: "ok"`
  - `version: <包版本>`
- 依赖：无
- 作用：探活/健康检查

#### 4.2 `POST /internal/scan`
- 输入 JSON：
  - `root_path` (string, 必填)：要扫描的根目录
  - `recursive` (bool, 默认 true)：是否递归子目录
  - `include_extensions` (string 数组, 可选)：扩展名白名单，小写比较，如 `["mkv","mp4"]`
  - `min_size_bytes` (u64, 可选)：最小文件大小，过滤小文件
- 输出 JSON：
  - `root_path`：请求的根目录
  - `files`: 数组，每项为：
    - `path`：字符串路径
    - `size_bytes`：文件大小（目录通常为 0）
    - `modified_at`：RFC3339 时间
    - `is_dir`：是否目录
- 依赖：
  - 文件系统可读
  - 运行用户需有遍历权限
- 结构/流程：
  1) 校验根路径存在
  2) WalkDir 遍历（可控制 max_depth）
  3) 对文件应用过滤（扩展名/最小大小）
  4) 收集 metadata，返回列表

### 5. 启动与配置
- 默认端口：`8088`（代码中写死，可后续改为配置/环境变量）
- 日志级别：默认 `info`，可通过 `RUST_LOG` 设置，如 `RUST_LOG=debug`

### 6. 错误处理
- 400：请求校验失败（如路径不存在）
- 500：内部错误（IO/时间转换/遍历异常等），日志记录具体原因
- 返回体统一为 `{ "message": "..." }`

### 7. 后续扩展预留
- 媒体解析接口 `/internal/analyze`（封装 `ffprobe`/`mediainfo`）
- 重命名/整理规划与执行接口
- 增量扫描（基于快照/事件监听）
- 可配置端口/并发度/超时等

