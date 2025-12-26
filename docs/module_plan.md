### 工程结构（KMM + Compose，多端）

```
root
├─ docs/                         # 文档
├─ shared/                       # 共享逻辑（KMP）
│  ├─ core/                      # 基础能力
│  │  ├─ model                   # 纯模型/类型
│  │  ├─ utils                   # 纯 Kotlin 工具
│  │  └─ ui                      # Compose 设计系统（跨端）
│  ├─ domain/                    # 业务抽象
│  │  └─ feed                    # 用例/接口
│  ├─ data/                      # 数据实现
│  │  └─ jikan                   # API/网络实现
│  ├─ feature/                   # 业务功能（UI+状态机）
│  │  ├─ discover
│  │  └─ anime-detail
│  └─ navigation                 # 路由模型（可选）
├─ apps/                         # 平台壳
│  ├─ android/                   # Android（demo/shelf 双入口 flavor）
│  ├─ desktop/                   # Compose Desktop
│  ├─ web/                       # Compose Web (JS)
│  └─ ios/                       # Compose iOS (UIKit)
└─ gradle/ 等                    # 构建配置
```

### 分层职责
- **shared/core**：最基础能力；`model/utils` 纯 Kotlin，无平台依赖；`ui` 提供跨端 Compose 设计系统。
- **shared/domain**：业务接口与用例（纯 Kotlin）。
- **shared/data**：数据源/仓库实现，当前 `data:jikan` 保留网络栈（Android 端实现）。
- **shared/feature**：Feature UI + 状态（MVI 风格），依赖 core/domain/data。
- **shared/navigation**：路由描述/常量，供各平台壳组装导航。
- **apps/**：仅做组装与平台导航；`android` 以 flavor 区分 demo/shelf；`desktop/web/ios` 复用 shared UI。

### 依赖方向（摘要）
- apps → feature → (core, domain) → core
- apps → data → domain
- feature 可直接依赖 data（推荐经由 domain 接口注入）
- core 不依赖 feature/domain/data/apps；domain 不依赖 Android/Compose。

### 版本与构建要点
- KMP + Compose Multiplatform，Android namespace/compileSdk 在各模块 `build.gradle.kts` 中声明。
- Compose dev 仓库：`https://maven.pkg.jetbrains.space/public/p/compose/dev` 已在 `settings.gradle.kts`。

