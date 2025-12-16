# Jikan API 字段分类文档

本文档详细说明了各个数据模型中哪些字段应该是必需的（非空），哪些是可选的（可空）。

## 分类原则

- ✅ **必需字段（非空）**: 核心标识字段，API 必定返回
- ⚠️ **可选字段（可空）**: 内容可能缺失、新作品数据不全、特定条件下才有的字段

---

## 1. Anime（动漫）

### 必需字段
- `malId: Int` - MAL 唯一标识符
- `url: String` - MAL 页面链接
- `images: Images` - 图片对象（API 会提供默认图片）
- `title: String` - 主标题
- `type: String` - 动漫类型（TV、Movie、OVA等）

### 可选字段
- `trailer: Trailer?` - 预告片（可能没有）
- `approved: Boolean?` - 审核状态
- `titles: List<Title>?` - 多语言标题列表
- `titleEnglish: String?` - 英文标题（可能没有翻译）
- `titleJapanese: String?` - 日文标题（可能没有）
- `titleSynonyms: List<String>?` - 别名列表
- `source: String?` - 原作来源
- `episodes: Int?` - 总集数（连载中未知）
- `status: String?` - 播放状态
- `airing: Boolean?` - 是否正在播放
- `aired: DateRange?` - 播放日期
- `duration: String?` - 单集时长
- `rating: String?` - 年龄分级
- `score: Double?` - 评分（新作品可能没有）
- `scoredBy: Int?` - 评分人数
- `rank: Int?` - 排名（需要足够评分）
- `popularity: Int?` - 人气排名
- `members: Int?` - 成员数
- `favorites: Int?` - 收藏数
- `synopsis: String?` - 简介（可能为空）
- `background: String?` - 背景信息
- `season: String?` - 播放季度
- `year: Int?` - 播放年份
- `broadcast: Broadcast?` - 播放时间
- `producers: List<MalUrl>?` - 制作公司
- `licensors: List<MalUrl>?` - 授权公司
- `studios: List<MalUrl>?` - 动画工作室
- `genres: List<MalUrl>?` - 类型标签
- `explicitGenres: List<MalUrl>?` - 显式类型
- `themes: List<MalUrl>?` - 主题标签
- `demographics: List<MalUrl>?` - 受众群体
- `relations: List<Relation>?` - 相关作品（仅 full 接口）
- `theme: AnimeTheme?` - 主题曲（仅 full 接口）
- `external: List<ExternalLink>?` - 外部链接
- `streaming: List<ExternalLink>?` - 流媒体链接

---

## 2. Manga（漫画）

### 必需字段
- `malId: Int` - MAL 唯一标识符
- `url: String` - MAL 页面链接
- `images: Images` - 图片对象
- `title: String` - 主标题
- `type: String` - 漫画类型（Manga、Novel等）

### 可选字段
- `approved: Boolean?` - 审核状态
- `titles: List<Title>?` - 多语言标题
- `titleEnglish: String?` - 英文标题
- `titleJapanese: String?` - 日文标题
- `titleSynonyms: List<String>?` - 别名
- `chapters: Int?` - 总章数（连载中未知）
- `volumes: Int?` - 总卷数
- `status: String?` - 连载状态
- `publishing: Boolean?` - 是否正在连载
- `published: DateRange?` - 出版日期
- `score: Double?` - 评分
- `scoredBy: Int?` - 评分人数
- `rank: Int?` - 排名
- `popularity: Int?` - 人气排名
- `members: Int?` - 成员数
- `favorites: Int?` - 收藏数
- `synopsis: String?` - 简介
- `background: String?` - 背景信息
- `authors: List<MalUrl>?` - 作者列表
- `serializations: List<MalUrl>?` - 连载杂志
- `genres: List<MalUrl>?` - 类型标签
- `explicitGenres: List<MalUrl>?` - 显式类型
- `themes: List<MalUrl>?` - 主题标签
- `demographics: List<MalUrl>?` - 受众群体
- `relations: List<Relation>?` - 相关作品
- `external: List<ExternalLink>?` - 外部链接

---

## 3. Character（角色）

### 必需字段
- `malId: Int` - MAL 唯一标识符
- `url: String` - MAL 页面链接
- `images: Images` - 角色图片
- `name: String` - 角色名称

### 可选字段
- `nameKanji: String?` - 日文名
- `nicknames: List<String>?` - 昵称列表
- `favorites: Int?` - 收藏数
- `about: String?` - 角色介绍
- `anime: List<CharacterAnimeEntry>?` - 出演动漫（仅 full 接口）
- `manga: List<CharacterMangaEntry>?` - 出现漫画（仅 full 接口）
- `voices: List<CharacterVoiceActor>?` - 声优列表（仅 full 接口）

---

## 4. Person（人物/声优）

### 必需字段
- `malId: Int` - MAL 唯一标识符
- `url: String` - MAL 页面链接
- `images: Images` - 人物图片
- `name: String` - 人物名称

### 可选字段
- `websiteUrl: String?` - 官方网站
- `givenName: String?` - 名
- `familyName: String?` - 姓
- `alternateNames: List<String>?` - 别名
- `birthday: String?` - 生日
- `favorites: Int?` - 收藏数
- `about: String?` - 人物介绍
- `anime: List<PersonAnimeEntry>?` - 参与动漫
- `manga: List<PersonMangaEntry>?` - 参与漫画
- `voices: List<PersonVoiceRole>?` - 配音角色

---

## 5. 通用数据模型（CommonModels）

### Images（图片信息）
**必需字段:**
- `jpg: ImageFormat` - JPG 格式图片（至少有一种格式）

**可选字段:**
- `webp: ImageFormat?` - WEBP 格式图片

### ImageFormat（图片格式）
**必需字段:**
- `imageUrl: String` - 普通尺寸图片 URL

**可选字段:**
- `smallImageUrl: String?` - 小尺寸
- `largeImageUrl: String?` - 大尺寸

### Title（标题）
**必需字段:**
- `title: String` - 标题文本

**可选字段:**
- `type: String?` - 标题类型

### MalUrl（MAL 链接对象）
**必需字段:**
- `malId: Int` - MAL ID
- `name: String` - 名称
- `url: String` - URL

**可选字段:**
- `type: String?` - 类型

### ExternalLink（外部链接）
**必需字段:**
- `name: String` - 链接名称
- `url: String` - 链接地址

### DateRange（日期范围）
**可选字段:**（所有字段都可能为空）
- `from: String?` - 开始日期
- `to: String?` - 结束日期（连载中为空）
- `prop: DateProp?` - 日期属性
- `string: String?` - 日期字符串

### Pagination（分页信息）
**必需字段:**
- `hasNextPage: Boolean` - 是否有下一页

**可选字段:**
- `lastVisiblePage: Int?` - 最后可见页
- `currentPage: Int?` - 当前页
- `items: PaginationItems?` - 分页项信息

---

## 6. User（用户）

### 必需字段
- `username: String` - 用户名
- `url: String` - MAL URL
- `images: Images` - 用户头像

### 可选字段
- `malId: Int?` - MAL ID（可能不返回）
- `lastOnline: String?` - 最后在线时间
- `gender: String?` - 性别
- `birthday: String?` - 生日
- `location: String?` - 所在地
- `joined: String?` - 加入日期
- `statistics: UserStatistics?` - 统计信息
- `favorites: UserFavorites?` - 收藏
- `about: String?` - 个人介绍
- `external: List<ExternalLink>?` - 外部链接

---

## 7. 其他数据模型

### Producer（制作公司）
**必需字段:**
- `malId: Int`
- `url: String`

**可选字段:** 其他所有字段

### Magazine（杂志）
**必需字段:**
- `malId: Int`
- `name: String`

**可选字段:** 其他所有字段

### Club（俱乐部）
**必需字段:**
- `malId: Int`
- `name: String`
- `url: String`

**可选字段:** 其他所有字段

### Season（季度）
**可选字段:**（所有字段都可能为空）
- `year: Int?`
- `seasons: List<String>?`

---

## 重构指导原则

1. **核心标识字段必须非空**: `malId`、`url`、`name`/`title`
2. **图片对象必须非空**: API 会提供默认图片
3. **统计和评分字段可空**: 新作品可能没有足够数据
4. **内容描述字段可空**: 可能没有简介或背景信息
5. **关联数据可空**: 不是所有作品都有相关作品或外部链接
6. **时间相关字段可空**: 连载中作品可能没有结束时间
7. **列表类型优先使用空列表**: 考虑使用 `= emptyList()` 而不是 `null`

---

## 注意事项

1. **Kotlin Serialization 默认值**: 设置了默认值的可选字段，在 JSON 中缺失时会使用默认值
2. **空列表 vs null**: 对于列表类型，建议使用 `= emptyList()` 作为默认值，避免空指针
3. **API 版本更新**: Jikan API 可能会更新，需要定期检查字段变化
4. **实际测试验证**: 建议实际调用 API 验证字段返回情况

---

最后更新: 2025-01-16

