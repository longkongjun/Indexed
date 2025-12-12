## MediaFlow 数据管理方案（整理工具版）

> 基于"只做整理，不做播放"的定位，定义最小化的数据存储与管理策略。

**职责边界**：详见 [system-boundary.md](./system-boundary.md)

---

## 1. 核心设计原则

### 1.1 最小化存储

**只存储整理所需的数据**，不存储展示/播放相关数据：
- ✅ 存储：文件路径、基础身份信息（title/year/外部ID）、技术信息（分辨率/编码）
- ❌ 不存储：详细简介、演员列表、高清海报、播放记录

### 1.2 单一职责

- **数据库用途**：支持整理任务的执行、防重复、审计、回滚
- **文件用途**：生成最小化 NFO，供媒体库快速识别

### 1.3 轻量级优先

- 表少、字段少、关系简单
- 快速启动、快速查询
- 便于备份与迁移

---

## 2. 数据库设计（精简版）

### 2.1 核心表（6 张）

#### 2.1.1 媒体条目表（media_items）

**用途**：记录所有需要整理的文件及其基础信息

| 字段 | 类型 | 说明 | 索引 |
|------|------|------|------|
| id | BIGINT | 主键，自增 | PK |
| type | VARCHAR(20) | 媒体类型：movie/tv/unclassified | INDEX |
| category | VARCHAR(50) | 二级分类：电影/动画电影/动漫/电视剧等 | INDEX |
| title | VARCHAR(500) | 标题（归一化） | INDEX |
| original_title | VARCHAR(500) | 原始标题 | |
| year | INT | 年份 | INDEX |
| season | INT | 季号（剧集用），NULL 表示电影 | |
| episode | INT | 集号（剧集用） | |
| tmdb_id | INT | TMDB ID | INDEX |
| imdb_id | VARCHAR(20) | IMDB ID（如 tt0111161） | INDEX |
| status | VARCHAR(20) | 状态：pending/organized/failed | INDEX |
| confidence_level | VARCHAR(10) | 置信度：P1/P2/P3/P4 | |
| source_path | TEXT | 原始文件路径 | |
| target_path | TEXT | 整理后目标路径 | INDEX |
| organize_method | VARCHAR(20) | 整理方式：hardlink/symlink/copy/move | |
| file_size | BIGINT | 文件大小（字节） | |
| file_hash | VARCHAR(64) | 文件哈希（SHA256，用于去重） | INDEX |
| created_at | TIMESTAMP | 发现时间 | INDEX |
| updated_at | TIMESTAMP | 更新时间 | |
| organized_at | TIMESTAMP | 整理完成时间 | |

**复合索引**：
- `(type, category, status)` - 分类查询
- `(title, year)` - 电影去重
- `(title, season, episode)` - 剧集去重

**说明**：
- 只存储"识别"所需的字段（title/year/外部ID）
- 不存储详细元数据（plot/rating/actors等）

#### 2.1.2 技术信息表（media_technical）

**用途**：存储命名与多版本区分所需的技术字段

| 字段 | 类型 | 说明 | 索引 |
|------|------|------|------|
| id | BIGINT | 主键，自增 | PK |
| media_item_id | BIGINT | 关联 media_items.id | FK, UNIQUE |
| resolution | VARCHAR(20) | 分辨率：2160p/1080p/720p | INDEX |
| source | VARCHAR(50) | 来源：BluRay/WEB-DL/WEBRip | INDEX |
| video_codec | VARCHAR(50) | 视频编码：x264/x265/AV1 | |
| audio_codec | VARCHAR(50) | 音频编码：AAC/DDP5.1 | |
| edition_tag | VARCHAR(100) | 版本标记：Extended/IMAX | |
| language_tag | VARCHAR(50) | 语言标记：CHS/CHT/ENG | |
| release_group | VARCHAR(100) | 发布组 | |
| duration_seconds | INT | 时长（秒） | |

**说明**：
- 仅用于生成文件名与区分多版本
- 不存储详细的流信息（码率/帧率等）

#### 2.1.3 类型关联表（media_genres）

**用途**：存储媒体的类型/体裁（用于二级目录分类）

| 字段 | 类型 | 说明 | 索引 |
|------|------|------|------|
| media_item_id | BIGINT | 关联 media_items.id | FK, INDEX |
| genre_name | VARCHAR(50) | 类型名称：Action/Drama/Animation | INDEX |
| priority | INT | 优先级（用于确定主分类） | |

**复合主键**：`(media_item_id, genre_name)`

**说明**：
- 简化设计，直接存储 genre_name，不单独建 genres 表
- 只用于二级目录分类（如动漫/电视剧/纪录片）

#### 2.1.4 整理任务表（organize_tasks）

**用途**：记录整理任务的执行状态

| 字段 | 类型 | 说明 | 索引 |
|------|------|------|------|
| id | BIGINT | 主键，自增 | PK |
| status | VARCHAR(20) | 状态：pending/running/completed/failed | INDEX |
| dry_run | BOOLEAN | 是否模拟执行 | |
| organize_method | VARCHAR(20) | 整理方式：hardlink/symlink/copy/move | |
| source_directory | TEXT | 源目录 | |
| target_directory | TEXT | 目标目录 | |
| total_items | INT | 总条目数 | |
| success_count | INT | 成功数 | |
| failed_count | INT | 失败数 | |
| skipped_count | INT | 跳过数 | |
| started_at | TIMESTAMP | 开始时间 | INDEX |
| finished_at | TIMESTAMP | 完成时间 | |
| error_message | TEXT | 错误信息 | |
| created_at | TIMESTAMP | 创建时间 | |

#### 2.1.5 整理条目表（organize_items）

**用途**：记录每个文件的整理详情（审计与回滚）

| 字段 | 类型 | 说明 | 索引 |
|------|------|------|------|
| id | BIGINT | 主键，自增 | PK |
| task_id | BIGINT | 关联 organize_tasks.id | FK, INDEX |
| media_item_id | BIGINT | 关联 media_items.id | FK, INDEX |
| source_path | TEXT | 源文件路径 | INDEX |
| target_path | TEXT | 目标文件路径 | INDEX |
| organize_method | VARCHAR(20) | 实际执行方式 | |
| status | VARCHAR(20) | 状态：success/failed/skipped | INDEX |
| conflict_resolution | VARCHAR(20) | 冲突处理：suffix/overwrite/skip | |
| file_size | BIGINT | 文件大小 | |
| error_message | TEXT | 错误信息 | |
| executed_at | TIMESTAMP | 执行时间 | INDEX |

**说明**：
- 用于审计、回滚、问题排查
- 关键表，不能省略

#### 2.1.6 刮削缓存表（scrape_cache）

**用途**：缓存外部 API 查询结果，减少重复请求

| 字段 | 类型 | 说明 | 索引 |
|------|------|------|------|
| id | BIGINT | 主键，自增 | PK |
| cache_key | VARCHAR(200) | 缓存键（normalized_title+year 或 tmdb_id） | UNIQUE |
| source | VARCHAR(20) | 数据源：tmdb/imdb/tvdb | INDEX |
| response_data | TEXT | 响应数据（JSON，仅基础字段） | |
| is_success | BOOLEAN | 是否成功响应 | INDEX |
| expires_at | TIMESTAMP | 过期时间 | INDEX |
| created_at | TIMESTAMP | 创建时间 | |

**缓存内容示例**（JSON）：
```json
{
  "title": "The Shawshank Redemption",
  "original_title": "The Shawshank Redemption",
  "year": 1994,
  "tmdb_id": 278,
  "imdb_id": "tt0111161",
  "genres": ["Drama", "Crime"]
}
```

**说明**：
- 只缓存基础信息（title/year/外部ID/genre）
- 不缓存详细信息（plot/actors等）

### 2.2 可选表（按需添加）

#### 2.2.1 配置表（settings）

**用途**：存储系统配置

| 字段 | 类型 | 说明 | 索引 |
|------|------|------|------|
| key | VARCHAR(100) | 配置键 | PK |
| value | TEXT | 配置值（JSON） | |
| description | TEXT | 配置描述 | |
| updated_at | TIMESTAMP | 更新时间 | |

**说明**：
- 也可以用配置文件（YAML/TOML）替代
- 如果需要动态配置（Web 界面修改），则需要此表

#### 2.2.2 审计日志表（audit_logs）

**用途**：记录关键操作（可选，用于审计）

| 字段 | 类型 | 说明 | 索引 |
|------|------|------|------|
| id | BIGINT | 主键，自增 | PK |
| action_type | VARCHAR(50) | 操作类型：organize/delete/update | INDEX |
| resource_type | VARCHAR(50) | 资源类型：media_item/task | |
| resource_id | BIGINT | 资源 ID | INDEX |
| before_data | TEXT | 操作前数据（JSON） | |
| after_data | TEXT | 操作后数据（JSON） | |
| created_at | TIMESTAMP | 操作时间 | INDEX |

**说明**：
- 可选表，对审计要求高时使用
- 简化版可以只记录到日志文件

### 2.3 ER 关系图（精简版）

```
media_items (核心表)
  ├── media_technical (1:1) - 技术信息
  ├── media_genres (1:N) - 类型关联
  └── organize_items (1:N) - 整理记录

organize_tasks (任务表)
  └── organize_items (1:N) - 任务明细

scrape_cache (缓存表) - 独立表
settings (配置表) - 独立表
audit_logs (审计表) - 可选
```

---

## 3. 数据更新策略

### 3.1 媒体条目新增/更新

#### 3.1.1 扫描新增文件

**触发时机**：
- 用户手动触发扫描
- 定时任务（每 5-10 分钟）
- 下载器 webhook 通知

**流程**：
```
1. 扫描目录 → 提取文件列表
2. 解析文件名 → 提取 title/year/season/episode/分辨率等
3. 计算文件哈希（可选，用于精确去重）
4. 查询数据库：按 (title, year, season, episode) 或 file_hash 去重
5. 若不存在 → 插入 media_items (status=pending)
6. 若存在且哈希不同 → 标记为多版本
7. 触发刮削（异步，见 3.2）
```

#### 3.1.2 刮削基础信息

**触发时机**：
- 新增媒体条目后自动触发
- 用户手动点击"刷新元数据"

**流程**：
```
1. 读取 media_items 的 title/year
2. 生成 cache_key = normalize(title) + year
3. 查询 scrape_cache：
   - 命中且未过期 → 使用缓存
   - 未命中或过期 → 调用外部 API（TMDB）
4. 解析响应（仅获取基础字段）：
   {
     "title": "...",
     "original_title": "...",
     "year": 1994,
     "tmdb_id": 278,
     "imdb_id": "tt0111161",
     "genres": ["Drama", "Crime"]
   }
5. 写入 scrape_cache (expires_at = NOW() + 7天)
6. 更新 media_items (tmdb_id/imdb_id/confidence_level)
7. 写入 media_genres (genre_name, priority)
```

**缓存策略**：
- 成功：缓存 7~30 天
- 失败：缓存 10~60 分钟
- 定期清理过期记录

#### 3.1.3 技术信息分析

**触发时机**：
- 新增条目后（可选）
- 整理前（用于命名）

**流程**：
```
1. 优先从文件名提取（零成本）
2. 若文件名不完整 → 调用 ffprobe/mediainfo
3. 写入 media_technical
```

### 3.2 整理任务执行

#### 3.2.1 生成整理计划

**触发时机**：
- 用户点击"整理媒体库"
- 定时任务自动整理

**流程**：
```
1. 查询 status=pending 的 media_items
2. 对每个条目：
   - 生成目标路径（按规则：Movies/电影/{Title} ({Year})/...）
   - 检测冲突（查询 target_path 是否存在）
   - 应用冲突策略（suffix/overwrite/skip）
3. 创建 organize_tasks (dry_run=true/false)
4. 批量插入 organize_items（记录源→目标映射）
5. 返回计划预览（Dry-run）或执行
```

#### 3.2.2 执行整理

**流程**：
```
1. 更新 organize_tasks (status=running, started_at=NOW())
2. 遍历 organize_items：
   - 创建目标目录
   - 执行硬链/软链/复制/移动
   - 更新 organize_items.status (success/failed/skipped)
   - 若成功 → 更新 media_items (target_path, status=organized, organized_at)
   - 生成 NFO 文件（见 4.2）
3. 更新 organize_tasks：
   - status=completed/failed
   - success_count/failed_count/skipped_count
   - finished_at=NOW()
```

#### 3.2.3 回滚支持

**流程**：
```
1. 查询 organize_items (task_id=X, status=success)
2. 对每个条目：
   - 删除目标文件（硬链/软链/复制）
   - 或移回原位置（移动模式）
3. 更新 media_items (target_path=NULL, status=pending)
4. 删除 NFO 文件
```

### 3.3 定时任务

| 任务 | 频率 | 说明 |
|------|------|------|
| **扫描新增文件** | 每 5-10 分钟 | 扫描下载目录 |
| **刮削缓存清理** | 每天凌晨 | 删除过期 scrape_cache |
| **孤儿文件清理** | 每周 | 清理 target_path 文件已删除的条目 |
| **数据库备份** | 每天凌晨 | 全量备份 |

---

## 4. 文件存储设计

### 4.1 NFO 文件（最小化）

#### 4.1.1 存储位置

**电影**：`Movies/电影/{Title} ({Year})/movie.nfo`  
**剧集**：
- 系列：`TV/{类型}/{ShowTitle}/tvshow.nfo`
- 单集：`TV/{类型}/{ShowTitle}/Season {Season}/S{Season}E{Episode}.nfo`

#### 4.1.2 最小化 NFO 内容

**电影 NFO 示例**：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<movie>
  <title>肖申克的救赎</title>
  <originaltitle>The Shawshank Redemption</originaltitle>
  <year>1994</year>
  <genre>剧情</genre>
  <genre>犯罪</genre>
  <uniqueid type="tmdb">278</uniqueid>
  <uniqueid type="imdb">tt0111161</uniqueid>
</movie>
```

**剧集 NFO 示例**：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<tvshow>
  <title>绝命毒师</title>
  <originaltitle>Breaking Bad</originaltitle>
  <year>2008</year>
  <genre>剧情</genre>
  <genre>犯罪</genre>
  <uniqueid type="tmdb">1396</uniqueid>
  <uniqueid type="imdb">tt0903747</uniqueid>
</tvshow>
```

**说明**：
- ❌ 不包含：plot/rating/actors/thumb 等
- ✅ 只包含：title/year/genre/外部ID
- **目的**：让媒体库快速识别，而不是替代媒体库的刮削

#### 4.1.3 生成时机

- 整理任务执行后自动生成
- 用户手动触发"重新生成 NFO"
- 刮削信息更新后重新生成

### 4.2 图片资源

**策略**：**不下载，不存储**

**原因**：
- 整理工具不需要图片来完成整理
- 媒体库会自己下载图片
- 避免重复存储与带宽浪费

**如果用户坚持要**：
- 可选功能：在整理后下载缩略图（poster.jpg）到媒体目录
- 只用于文件管理器预览，不用于媒体库

### 4.3 字幕文件

**存储位置**：与视频同目录，同名前缀

**示例**：
```
Movies/电影/肖申克的救赎 (1994)/
  ├── 肖申克的救赎.1994.1080p.BluRay.x265.mkv
  ├── 肖申克的救赎.1994.1080p.BluRay.x265.zh-CN.srt
  └── 肖申克的救赎.1994.1080p.BluRay.x265.en.srt
```

**处理策略**：
- 整理时识别同名字幕（`{视频文件名}*.srt`）
- 一并移动/链接到目标目录
- 在 media_technical 表记录字幕语言（可选）

### 4.4 数据库备份

**备份策略**：
- 全量备份：每天凌晨
- 保留时间：30 天
- 存储位置：`/backup/db/`

**备份内容**：
- 所有表数据（SQL Dump）
- 配置文件（如果用文件配置）

### 4.5 日志文件

**日志类型**：
- 应用日志：`/logs/app-{date}.log`（按天滚动，保留 30 天）
- 错误日志：`/logs/error-{date}.log`（按天滚动，保留 90 天）

**日志级别**：
- 生产：INFO + WARN
- 开发：DEBUG

---

## 5. 性能优化

### 5.1 索引优化

**高频查询字段**：
- `media_items`: type, category, status, title, year, target_path, file_hash
- `organize_items`: source_path, target_path, status
- `scrape_cache`: cache_key, expires_at

**复合索引**：
- `(type, category, status)` - 分类查询
- `(title, year)` - 电影去重
- `(title, season, episode)` - 剧集去重

### 5.2 查询优化

- **分页查询**：使用 LIMIT/OFFSET
- **避免全表扫描**：WHERE 条件使用索引字段
- **JOIN 优化**：media_items LEFT JOIN media_technical（小表 JOIN）

### 5.3 缓存策略

- **数据库缓存**：scrape_cache 表
- **应用层缓存**：Redis 缓存热点数据（可选）
  - 分类统计（各类型媒体数量）
  - 最近整理的媒体列表

### 5.4 批量操作

- **批量插入**：扫描新增文件时，批量 INSERT
- **批量更新**：整理任务执行时，批量更新 status
- **事务控制**：整理任务在事务中执行，确保一致性

---

## 6. 数据一致性

### 6.1 外键约束

- `media_technical.media_item_id` → `media_items.id` (ON DELETE CASCADE)
- `media_genres.media_item_id` → `media_items.id` (ON DELETE CASCADE)
- `organize_items.task_id` → `organize_tasks.id` (ON DELETE CASCADE)
- `organize_items.media_item_id` → `media_items.id` (ON DELETE SET NULL)

### 6.2 事务管理

**必须在事务中执行的操作**：
- 整理任务执行（更新多个表）
- 刮削信息更新（更新 media_items + media_genres）
- 批量插入/更新

### 6.3 定期校验

**校验任务**（每周执行）：
- 检查 target_path 文件是否存在
- 清理 source_path 已删除的条目
- 清理孤儿记录（media_technical 无对应 media_items）

---

## 7. 与媒体库的交互

### 7.1 输出给媒体库

**整理工具生成**：
- 规范的目录结构
- 最小化 NFO 文件（title/year/外部ID/genre）
- 字幕文件（一并整理）

**媒体库读取**：
- 扫描目录，发现新文件
- 读取 NFO，快速识别（通过外部ID）
- 自己补充详细刮削（plot/actors/images）

### 7.2 不依赖媒体库

**整理工具独立运行**：
- 不需要媒体库运行即可工作
- 不需要连接媒体库 API
- 不读取媒体库数据库

### 7.3 支持多种媒体库

**生成标准 NFO**：
- Jellyfin/Plex/Emby/Kodi 都能识别
- 用户可以自由选择媒体库

---

## 8. 总结：数据边界

### 8.1 数据库存储内容

| 内容 | 存储 | 不存储 |
|------|------|--------|
| **文件路径** | ✅ source_path, target_path | |
| **基础身份** | ✅ title, year, 外部ID | ❌ 详细简介 |
| **技术信息** | ✅ 分辨率, 编码, 来源 | ❌ 详细流信息 |
| **类型分类** | ✅ genre (用于二级目录) | ❌ 详细分类树 |
| **整理记录** | ✅ 任务状态, 审计日志 | |
| **刮削缓存** | ✅ 基础响应 | ❌ 详细响应 |
| **图片** | ❌ 不存储 | |
| **演员/导演** | ❌ 不存储 | |
| **播放记录** | ❌ 不存储 | |

### 8.2 文件存储内容

| 内容 | 存储 | 不存储 |
|------|------|--------|
| **NFO 文件** | ✅ 最小化 NFO | ❌ 完整 NFO |
| **字幕文件** | ✅ 与视频一起整理 | |
| **图片** | ❌ 不下载（除非可选功能） | |
| **日志** | ✅ 应用日志 | |
| **备份** | ✅ 数据库备份 | |

### 8.3 核心优势

1. **轻量级**：6 张核心表，数据量小，启动快
2. **职责清晰**：只做整理，不替代媒体库
3. **易维护**：表少、关系简单、逻辑清晰
4. **可扩展**：后续如需功能，可按需增加表
5. **兼容性强**：生成标准 NFO，支持所有媒体库

---

## 附录：SQL 示例

### 创建表（PostgreSQL）

```sql
-- 媒体条目表
CREATE TABLE media_items (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    category VARCHAR(50),
    title VARCHAR(500) NOT NULL,
    original_title VARCHAR(500),
    year INT,
    season INT,
    episode INT,
    tmdb_id INT,
    imdb_id VARCHAR(20),
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    confidence_level VARCHAR(10),
    source_path TEXT NOT NULL,
    target_path TEXT,
    organize_method VARCHAR(20),
    file_size BIGINT,
    file_hash VARCHAR(64),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    organized_at TIMESTAMP
);

CREATE INDEX idx_media_type ON media_items(type);
CREATE INDEX idx_media_category ON media_items(category);
CREATE INDEX idx_media_status ON media_items(status);
CREATE INDEX idx_media_title ON media_items(title);
CREATE INDEX idx_media_year ON media_items(year);
CREATE INDEX idx_media_tmdb ON media_items(tmdb_id);
CREATE INDEX idx_media_hash ON media_items(file_hash);
CREATE INDEX idx_media_target ON media_items(target_path);
CREATE INDEX idx_media_type_cat_status ON media_items(type, category, status);
CREATE INDEX idx_media_title_year ON media_items(title, year);

-- 技术信息表
CREATE TABLE media_technical (
    id BIGSERIAL PRIMARY KEY,
    media_item_id BIGINT NOT NULL UNIQUE,
    resolution VARCHAR(20),
    source VARCHAR(50),
    video_codec VARCHAR(50),
    audio_codec VARCHAR(50),
    edition_tag VARCHAR(100),
    language_tag VARCHAR(50),
    release_group VARCHAR(100),
    duration_seconds INT,
    FOREIGN KEY (media_item_id) REFERENCES media_items(id) ON DELETE CASCADE
);

CREATE INDEX idx_tech_resolution ON media_technical(resolution);
CREATE INDEX idx_tech_source ON media_technical(source);

-- 类型关联表
CREATE TABLE media_genres (
    media_item_id BIGINT NOT NULL,
    genre_name VARCHAR(50) NOT NULL,
    priority INT DEFAULT 0,
    PRIMARY KEY (media_item_id, genre_name),
    FOREIGN KEY (media_item_id) REFERENCES media_items(id) ON DELETE CASCADE
);

CREATE INDEX idx_genre_name ON media_genres(genre_name);

-- 整理任务表
CREATE TABLE organize_tasks (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    dry_run BOOLEAN NOT NULL DEFAULT false,
    organize_method VARCHAR(20),
    source_directory TEXT,
    target_directory TEXT,
    total_items INT DEFAULT 0,
    success_count INT DEFAULT 0,
    failed_count INT DEFAULT 0,
    skipped_count INT DEFAULT 0,
    started_at TIMESTAMP,
    finished_at TIMESTAMP,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_task_status ON organize_tasks(status);
CREATE INDEX idx_task_started ON organize_tasks(started_at);

-- 整理条目表
CREATE TABLE organize_items (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    media_item_id BIGINT,
    source_path TEXT NOT NULL,
    target_path TEXT,
    organize_method VARCHAR(20),
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    conflict_resolution VARCHAR(20),
    file_size BIGINT,
    error_message TEXT,
    executed_at TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES organize_tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (media_item_id) REFERENCES media_items(id) ON DELETE SET NULL
);

CREATE INDEX idx_item_task ON organize_items(task_id);
CREATE INDEX idx_item_media ON organize_items(media_item_id);
CREATE INDEX idx_item_status ON organize_items(status);
CREATE INDEX idx_item_source ON organize_items(source_path(255));
CREATE INDEX idx_item_target ON organize_items(target_path(255));

-- 刮削缓存表
CREATE TABLE scrape_cache (
    id BIGSERIAL PRIMARY KEY,
    cache_key VARCHAR(200) NOT NULL UNIQUE,
    source VARCHAR(20) NOT NULL,
    response_data TEXT,
    is_success BOOLEAN NOT NULL DEFAULT true,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_cache_source ON scrape_cache(source);
CREATE INDEX idx_cache_expires ON scrape_cache(expires_at);
```

### 常用查询示例

```sql
-- 查询待整理的电影
SELECT id, title, year, source_path
FROM media_items
WHERE type = 'movie' AND status = 'pending'
ORDER BY created_at DESC
LIMIT 50;

-- 查询某剧集的所有集
SELECT season, episode, target_path, organized_at
FROM media_items
WHERE title = 'Breaking Bad' AND type = 'tv'
ORDER BY season, episode;

-- 统计各分类的媒体数量
SELECT category, COUNT(*) as count
FROM media_items
WHERE type = 'tv' AND status = 'organized'
GROUP BY category
ORDER BY count DESC;

-- 查询最近的整理任务
SELECT id, status, total_items, success_count, failed_count, started_at, finished_at
FROM organize_tasks
ORDER BY created_at DESC
LIMIT 10;

-- 清理过期缓存
DELETE FROM scrape_cache
WHERE expires_at < NOW();
```

