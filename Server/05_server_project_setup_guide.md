# 服务端项目创建指南（分步）

本文档指导你**按步骤、按自己的选择**创建服务端项目，不直接生成完整项目，每一步都会说明要做的事和需要你做的决定。技术栈以 [01_server_architecture.md](../plan/01_server_architecture.md) 为准：**Ktor (Kotlin)**、**SQLite/PostgreSQL**、本地文件系统。

---

## 前置条件

**开发/构建环境**（在你自己的机器上写代码、打镜像时）：

- 已安装 **JDK 17+**（推荐 17 或 21）
- 已安装 **Kotlin**（或仅用 JDK 自带的 Kotlin 编译，由构建工具拉取）
- 选择其一：**Gradle**（推荐）或 **Maven**

**运行环境**（最终部署的机器）：见下方「[关于部署：Docker 与无 JDK 环境](#关于部署docker-与无-jdk-环境)」。若用 Docker，运行机器**不需要**安装 JDK/JRE。

确认后进入下一步。

---

## 第一步：项目位置与构建系统

**你要做的：**

1. 在仓库根目录下创建服务端项目目录，例如：
   - `Server/`（与 Admin、Client 并列），或
   - `server/`（小写，看仓库命名风格）
2. 决定构建工具：
   - **Gradle**（推荐）：用 Kotlin DSL 的 `build.gradle.kts`，与 Kotlin 生态一致
   - **Maven**：`pom.xml`

**需要你确认：**

- 项目目录名：`Server` 还是 `server`？（与现有 plan 中「Server」对应即可）
- 构建工具：Gradle 还是 Maven？

确认后，在所选目录下**仅**创建构建脚本骨架（不写业务代码）：
- Gradle：`settings.gradle.kts`、`build.gradle.kts`、`gradle/wrapper`（可用 `gradle wrapper` 生成）
- Maven：`pom.xml`（packaging 为可执行 jar，并配置 Kotlin 与 exec 插件）

---

## 第二步：初始化 Ktor 工程

**你要做的：**

1. 在第一步的目录里，用官方方式初始化 Ktor 项目（或手写最小依赖）：
   - [Ktor 官方文档](https://ktor.io/docs/create-project.html) 中的「Create a new project」可生成 Gradle/Maven 项目，可把生成内容放到你的 `Server/` 下覆盖/合并。
2. 选择 **Engine**：
   - **CIO**（推荐）、Netty 或 Jetty；个人本地部署用 CIO 即可。详细对比见 [附录：CIO / Netty / Jetty / Tomcat 对比](#附录cio--netty--jetty--tomcat-对比)。
3. 在 `build.gradle.kts`（或 `pom.xml`）中只保留当前步骤需要的依赖：
   - `ktor-server-core`
   - `ktor-server-cio`（或你选的 engine）
   - `ktor-server-status-pages`（可选，用于统一错误码）
   - Kotlin 版本与架构文档中的目标一致（如 1.9+）。

**需要你确认：**

- Engine：**CIO（已选）** / Netty / Jetty
- 是否在本步骤就加 **Content Negotiation**（如 `kotlinx.serialization`）？建议：**是**，便于后面统一 JSON 协议。

完成本步后，应能运行一个「Hello World」或空路由的 main，并能在浏览器访问到响应。

---

## 第三步：项目包结构与模块划分

**你要做的：**

1. 在 `src/main/kotlin`（或 `src/main/java`）下按**包**做逻辑划分（先不拆多模块，单模块即可）：
   - `api`：路由、鉴权入口
   - `auth`：认证服务
   - `resource`：资源管理
   - `plugin`：插件服务
   - `rule`：规则引擎
   - `metadata`：元数据刮削
   - `storage`：存储/文件读写
   - `scan`：扫描/识别
   - `organize`：分类/整理
   - `config`：配置加载
   - `db` 或 `persistence`：数据库访问
2. 可根据「先做最小闭环」的原则，先只建其中几个包（例如 `api`、`config`、`db`），其余在实现时再建。

**需要你确认：**

- 包名根：例如 `com.indexed.server` 或 `io.indexed.server`？
- 是否在本步骤就建齐上述所有包（空目录/空文件占位），还是只建当前要用的？

确认后，只创建包目录和（可选）空 `Application.kt` 或 `Main.kt` 入口，不实现业务逻辑。

---

## 第四步：数据库与持久层

**你要做的：**

1. 选数据库：
   - **SQLite**（推荐，单机、零运维）
   - 或 **PostgreSQL**（若你计划多机或已有 PG）
2. 选持久层方式：
   - **Exposed**（Kotlin 惯用，与 Ktor 搭配多）
   - 或 **JDBC + 手写 SQL**、或 **JPA**（若团队更熟 JPA）
3. 在 `build.gradle.kts` 中加入对应依赖，并增加：
   - 数据库连接配置（如 JDBC URL、文件路径）
   - 一个最小表（如 `health` 或 `config`）用于验证读写，不写业务表

**需要你确认：**

- 数据库：SQLite 还是 PostgreSQL？
- 持久层：Exposed / 纯 JDBC / JPA？
- 配置文件格式：`application.conf`（HOCON）、`application.yaml` 或环境变量？建议：**application.conf**，与 Ktor 默认一致。

完成本步后，应用启动时能连上数据库并执行一次简单查询。

---

## 第五步：统一 API 入口与协议

**你要做的：**

1. 在 `api` 包下做路由挂载：
   - 例如统一前缀：`/api/v1`（与架构中的「可选 API 版本前缀」一致）
   - 健康检查：`GET /api/v1/health` 或 `GET /health`（可不带版本）
2. 统一协议形状（与架构文档一致）：
   - 成功：`{ "data": ..., "code": 0 }` 或你约定的格式
   - 错误：`{ "code": 非0, "message": "..." }`
   - 配置 CORS（若前端会跨域）
3. 鉴权：本步可只做「占位」：
   - 单用户可先固定 Token 或关闭鉴权，在路由里留一个 `authenticate { }` 或等价逻辑，后续再接认证服务。

**需要你确认：**

- API 版本前缀：是否用 `/api/v1`？还有无其他前缀（如 `/admin`）？
- 健康检查路径：`/health` 还是 `/api/v1/health`？
- 单用户时是否在本步骤就「关闭鉴权」或「固定 Token」？

完成本步后，用 curl 或浏览器能访问健康检查并得到统一格式的 JSON。

---

## 第六步：配置与运行方式

**你要做的：**

1. 确定配置来源：
   - `application.conf`：端口、数据库 URL、库根目录等
   - 环境变量覆盖（如 `PORT`、`DB_PATH`）
2. 打包与运行：
   - Gradle：`./gradlew run` 或 `shadowJar` 后 `java -jar server.jar`
   - Maven：`mvn exec:java` 或 `mvn package` 后 `java -jar ...`
3. 如有需要，在 `README` 或 `docs` 中写一句「如何启动服务端」（一行命令即可）。

**需要你确认：**

- 默认端口：例如 `8080`？
- 库根目录（存储服务用）：默认路径或仅占位配置项，暂不读写真实目录？

完成本步后，能通过配置文件或环境变量改端口并启动。

---

## 关于部署：Docker 与无 JDK 环境

本指南搭出的服务端**支持通过 Docker 部署**，且架构文档中已约定「单进程 / Docker（可选）」。若部署机器上没有 JDK/JRE，可以用下面几种方式。

### 方式一：Docker 部署（推荐，无需在机器上装 JDK）

- **思路**：在镜像里自带 JRE（或 JDK），宿主机只需安装 Docker，不需要装 Java。
- **你要做的**：
  - 在 `Server/` 下增加 `Dockerfile`：多阶段构建，先阶段用 Gradle/Maven 打出一个可执行 jar（或 fat jar），再阶段用带 JRE 的基础镜像（如 `eclipse-temurin:17-jre-alpine`）只拷贝 jar 和启动命令。
  - 通过环境变量或挂载配置文件传入端口、数据库路径、库根目录等；库根目录需用 `-v` 挂载到容器内，便于访问宿主机上的资源文件。
- **运行示例**（仅示意）：  
  `docker build -t indexed-server ./Server && docker run -p 8080:8080 -v /path/to/source:/data/source -v /path/to/organized:/data/organized indexed-server`
- 这样**运行服务的机器上不需要安装 JDK 或 JRE**，只要 Docker 即可。

### 方式二：仅 JRE 运行（机器上有 JRE 即可，无需 JDK）

- 在**有 JDK 的机器**上先打包：`./gradlew shadowJar` 或 `mvn package`，得到可执行 jar。
- 把该 jar 拷贝到部署机器，在部署机器上安装 **JRE 17+**（不必装 JDK），执行：  
  `java -jar server.jar`
- 适合不想用 Docker、但部署机可以装 JRE 的场景。

### 方式三：GraalVM Native Image（可选，进阶）

- 将应用编译为**原生可执行文件**（无 JVM），部署机既不需要 JDK 也不需要 JRE。
- 需要引入 GraalVM 与 Native Image 插件、处理反射/资源等配置，构建较慢、调试成本高，可作为后续优化项，不必在第一步就做。

### 小结

| 部署方式 | 运行机是否需要 JDK/JRE | 说明 |
|----------|-------------------------|------|
| **Docker** | 否，只需 Docker | 推荐；镜像内带 JRE，库根目录需挂载 |
| **java -jar** | 需要 JRE 17+ | 简单；需在构建机打 jar，再拷贝到运行机 |
| **Native Image** | 否 | 可选；构建复杂，适合追求冷启动与体积时再考虑 |

若你希望「机器上没有 JDK 也能跑」，优先在第六步或后续步骤中加上 **Dockerfile + 多阶段构建**，并在小结表中增加「是否提供 Docker 镜像」的选项。

---

## 程序应定义的基本目录与用户可配置项

### 使用者关心的目录（仅两个）

| 目录 | 用途 | 说明 |
|------|------|------|
| **资源来源目录（source）** | 原始文件 | 用户资源所在目录，程序**只读**，不修改 |
| **整理输出目录（scraping.organized）** | 整理后展示 | 程序以链接/复制方式引用资源并写入刮削元数据，供「已整理」视图 |

使用者只需在配置或环境变量中指定这两项，其余目录不需要关心。

### 程序内部目录（仅开发者需关心）

data、logs、cache、temp 为程序内部使用，**不向使用者暴露**，**程序内固定**，不读环境变量与配置文件：

- **应用主目录** base = `{user.home}/.indexed`
- **内部路径**：data、logs、cache、temp 为 base 下子目录（`{base}/data` 等）

Docker 下若需持久化，可将该目录挂载为卷，例如 `-v /宿主机路径:/root/.indexed`（以 root 运行容器时）。

### 资源来源目录 vs 整理输出目录

- **资源来源目录**：用户自己维护的原始文件（漫画、影视等）。程序只做扫描、识别、刮削元数据，**不移动、不删除、不修改**该目录下内容。
- **整理输出目录**：程序根据规则与刮削结果，在该目录下生成「整理后的目录结构」，通过**硬链接 / 软链接 / 复制**等方式引用资源来源中的文件，并写入刮削得到的元数据（如 NFO、封面等）。客户端/后台展示的「已整理」视图基于此目录。

---

## 服务端文件管理与 Docker 卷

### 运行时：服务进程和文件是怎么交互的

可以记住一句话：**服务程序只是一个普通进程，它用「某个路径」去读/写文件，操作系统负责把这次读/写落到真正的磁盘上。** 交互关系如下。

1. **程序里做的事**  
   例如配置里资源来源根是 `/data/source`，数据库里有一条资源相对路径 `comics/某漫画/第1话.pdf`。  
   存储服务会拼出绝对路径：`/data/source/comics/某漫画/第1话.pdf`，然后像平时写代码一样：
   - 读：`File(path).inputStream().use { ... }` 或 `Files.newInputStream(Path.of(path))`
   - 写：`File(path).outputStream().use { ... }`
   - 列表：`File(path).listFiles()`  
   程序**不关心**这个路径是「本机」还是「容器里」的，它只认「当前进程能看到的路径」。

2. **进程「看到的」是哪个路径**  
   - **本机直接运行**：进程跑在你这台机器上，看到的文件系统就是本机磁盘。  
     你配置 `SOURCE_ROOT=/home/user/comics`，那 `File("/home/user/comics/xxx")` 读的就是本机 `/home/user/comics/xxx`，数据从本机磁盘读出来。
   - **Docker 里运行**：进程跑在**容器内部**，看到的文件系统是**容器自己的根文件系统**。  
     你 `docker run -v /home/user/comics:/data/source ...` 之后，容器里就多了一个目录 `/data/source`，它和宿主机的 `/home/user/comics` **是同一块磁盘内容**（ bind mount）。  
     程序里配置 `SOURCE_ROOT=/data/source`，然后 `File("/data/source/xxx")`：  
     - 在进程看来：读的是「容器里的 `/data/source/xxx`」；  
     - 在操作系统看来：这个路径被挂载到了宿主机 `/home/user/comics`，所以实际读的是**宿主机**上的 `/home/user/comics/xxx`，数据从宿主机的磁盘读出来。

3. **数据流一句话**  
   - 本机运行：**程序读/写路径 P → 操作系统 → 本机磁盘上的 P**。  
   - Docker 运行：**程序读/写路径 P（容器内路径）→ 操作系统发现 P 是挂载点 → 转到宿主机对应目录 → 宿主机磁盘**。  
   所以：和「服务器上的文件」交互，就是**进程对某个路径做读/写，该路径在本机就是本机磁盘，在 Docker 里就通过卷映射到宿主机磁盘**，程序代码不用改，只要把「资源来源根」配成当前环境能看到的路径即可。

### 服务端文件怎么管理（配置与约定）

- **资源来源目录（source）**：架构里原始资源文件都来自此目录。服务端**不写死路径**，通过**配置**指定（如 `application.yaml` 或环境变量 `SOURCE_ROOT`）。
- **整理输出目录（organized）**：刮削与整理后的展示目录，配置键 `indexed.scraping.organized` 或环境变量 `ORGANIZED_PATH`。
- **存储服务**：对资源来源只做「在来源根下的相对路径」的读；对整理输出做写（链接/复制 + 元数据）。
- **路径约定**：
  - 数据库/配置里只存**相对资源来源根的路径**（如 `comics/某漫画/第1话.pdf`），不存宿主机绝对路径，这样换机器或 Docker 换挂载点也不用改数据。
  - 运行时用「当前资源来源根 + 相对路径」拼出真实路径再读；整理输出用「整理输出目录 + 整理后结构」写。

### Docker 部署时卷怎么处理

- **目的**：容器内没有宿主机上的资源目录，所以要用 **卷挂载** 把宿主机的目录「映射」进容器，让服务端进程在容器里访问到的路径实际指向宿主机磁盘。
- **做法**：
  - `docker run -v 宿主机路径:容器内路径 ...`  
    例如：`-v /home/user/comics:/data/source -v /home/user/organized:/data/organized`  
    表示：宿主机资源目录在容器里为 `/data/source`，整理输出目录在容器里为 `/data/organized`。
  - 服务端**配置里填容器内路径**（如 `SOURCE_ROOT=/data/source`、`ORGANIZED_PATH=/data/organized`），应用只认容器内路径；真实文件在宿主机，由 Docker 透明地挂载进来。
- **数据库/配置**：若希望数据持久、不随容器删除而丢，也可把数据库文件或配置目录挂载出去，例如：  
  `-v /host/app/data:/data/app`，应用把 SQLite 或 `application.yaml` 放在 `/data/app`，即落在宿主机 `/host/app/data`。

### 小结

| 环境 | 资源来源/整理输出在配置里的值 | 实际文件位置 |
|------|-------------------------------|--------------|
| 本机直接运行 | 本机绝对路径，如 `/home/user/comics`、`/home/user/organized` | 本机磁盘 |
| Docker 运行 | **容器内**路径，如 `/data/source`、`/data/organized` | 宿主机目录通过 `-v` 挂载到容器内对应路径 |

这样同一套「来源根 + 相对路径」「整理输出目录 + 结构」的逻辑，本机与 Docker 通用；只有路径本身通过配置/环境变量区分本机还是容器内。

---

## 后续步骤（按架构文档实现）

按 [01_server_architecture.md](../plan/01_server_architecture.md) 中的模块顺序或产品优先级实现即可，建议顺序：

1. **存储服务**：读库根、列表、流式读文件（与「库根目录」配置联动）
2. **资源管理服务**：资源 CRUD、与存储的对接
3. **扫描/识别**：遍历库根、调用识别逻辑（可先做一种资源类型）
4. **认证服务**：若需多用户再上 JWT/会话；单用户可一直用固定 Token 或关闭
5. **插件服务、规则引擎、元数据、分类/整理**：按需求依次加

每加一个模块，都在对应包下实现，并在 `api` 层挂载路由；鉴权在 API 网关层统一做。

---

## 小结：你需要先定的几件事

| 步骤 | 你的选择 |
|------|----------|
| 1 | 项目目录名、Gradle/Maven |
| 2 | Ktor Engine：**CIO（已选）**、是否加 Content Negotiation |
| 3 | 包名根、是否建齐所有包 |
| 4 | SQLite/PostgreSQL、Exposed/JDBC/JPA、配置格式 |
| 5 | API 前缀、健康检查路径、鉴权策略 |
| 6 | 默认端口、库根目录占位；是否提供 Dockerfile |
| （部署） | Docker / JRE / Native Image |

按上述步骤依次做决定并执行，即可在「不直接生成整项目」的前提下，一步步搭出符合架构文档的服务端骨架。若你告诉我「第一步选 Gradle + Server 目录」，我可以从第一步的构建脚本开始，只生成该步的最小内容，再等你确认下一步。

---

## 附录：CIO / Netty / Jetty / Tomcat 对比

以下为 **Ktor 可用引擎**（CIO、Netty、Jetty）与 **传统 Servlet 容器 Tomcat** 的优缺点对比，供第二步选型参考。Ktor 若用 Tomcat，需通过 **Servlet 引擎** 运行在 Tomcat 上，用法与前三者略有不同。

| 维度 | CIO | Netty | Jetty | Tomcat |
|------|-----|-------|-------|--------|
| **定位** | Ktor 自研、协程原生 | 通用 NIO 框架，Ktor 封装为引擎 | 轻量嵌入式 / Servlet 容器 | 经典 Servlet 容器 |
| **与 Ktor 关系** | 官方首选、为 Ktor 定制 | 官方支持，成熟稳定 | 官方支持 | 通过 Ktor Servlet 引擎运行 |
| **并发模型** | Kotlin 协程（挂起、少线程） | 事件循环 + NIO | 异步 IO + 线程池 | 传统请求一线程（BIO/NIO 可选） |
| **依赖体积** | 较小 | 较大（Netty 自身较重） | 中等 | 大（完整 Servlet 栈） |
| **学习/运维成本** | 低（与 Ktor 一体） | 中（可深挖 Netty 调优） | 中 | 高（概念多、配置多） |
| **性能（高并发）** | 好，协程轻量 | 很好，久经考验 | 好 | 一般（默认模型下线程多） |
| **适用场景** | 个人/中小项目、本地部署 | 高并发、长连接、需要 Netty 生态 | 嵌入式、需要 Servlet 兼容 | 企业遗留、Spring MVC 等 Servlet 应用 |

### CIO（Coroutine-based I/O）

- **优点**：与 Kotlin 协程深度集成，代码风格统一（挂起函数）；依赖少、启动快；Ktor 文档与示例多以 CIO 为主；无 Netty 的 ByteBuf 等概念，心智负担小。
- **缺点**：生态与调优资料不如 Netty 多；极端高并发、需要精细控制底层 IO 时，可选项不如 Netty 丰富。
- **适合**：个人本地部署、中小规模、希望「用 Ktor 就完事」的项目（与当前架构文档推荐一致）。

### Netty

- **优点**：久经生产验证，高并发、长连接（WebSocket、gRPC）场景多；可精细调优；社区与资料多。
- **缺点**：依赖体积大；底层概念多（EventLoop、ByteBuf、Channel 等），若只写业务 API 不一定用得上；与 Ktor 协程的衔接需要理解线程与调度。
- **适合**：预期高并发、大量 WebSocket/流式、或团队已有 Netty 经验时选用。

### Jetty

- **优点**：轻量、易嵌入；支持 Servlet 与异步 Servlet；适合内嵌到应用或与现有 Java 生态集成。
- **缺点**：在纯 Ktor 场景下相比 CIO 没有明显优势；若不需要 Servlet 兼容，选 CIO 更简单。
- **适合**：需要与 Servlet 规范兼容、或已有 Jetty 运维经验的场景。

### Tomcat

- **优点**：使用最广的 Servlet 容器之一，企业运维熟悉；与 Spring Boot 等默认选型一致；功能全（JSP、WebSocket、NIO 等）。
- **缺点**：重量级；与 Ktor 的「协程 + 轻量」风格不搭，需走 Servlet 引擎，Ktor 官方示例和文档较少以 Tomcat 为主；传统一线程一请求模型下，高并发需调优（NIO 等）。
- **适合**：已有 Tomcat 基础设施、或必须跑在 Servlet 容器上的场景；**新建 Ktor 项目一般不首选 Tomcat**。

### 小结（结合本仓库）

- **Indexed 服务端**：个人本地部署、单进程、无极高并发要求 → 选 **CIO** 即可，简单、与 Ktor/Kotlin 一致。
- 若后续有大量 WebSocket、流式推送或超高 QPS，再评估 **Netty**；若需 Servlet 兼容或迁入现有 Tomcat，再考虑 **Jetty / Tomcat**。

---

**最后更新**：2026-02-13 · 版本 1.0.0
