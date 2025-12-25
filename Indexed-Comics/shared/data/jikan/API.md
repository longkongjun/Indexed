# Jikan API 接口文档

本文档详细说明了 Jikan API Kotlin 客户端的所有接口定义。

## 📋 目录

- [概述](#概述)
- [Anime API - 动漫接口](#anime-api---动漫接口)
- [Manga API - 漫画接口](#manga-api---漫画接口)
- [Character API - 角色接口](#character-api---角色接口)
- [People API - 人物接口](#people-api---人物接口)
- [Season API - 季度接口](#season-api---季度接口)
- [Producer API - 制作公司接口](#producer-api---制作公司接口)
- [Magazine API - 杂志接口](#magazine-api---杂志接口)
- [Club API - 俱乐部接口](#club-api---俱乐部接口)
- [User API - 用户接口](#user-api---用户接口)
- [Watch API - 观看推荐接口](#watch-api---观看推荐接口)
- [Genres API - 类型接口](#genres-api---类型接口)
- [Random API - 随机接口](#random-api---随机接口)
- [Recommendations API - 推荐接口](#recommendations-api---推荐接口)
- [Reviews API - 评论接口](#reviews-api---评论接口)
- [Schedules API - 时间表接口](#schedules-api---时间表接口)
- [Top API - 排行榜接口](#top-api---排行榜接口)

---

## 概述

### 基本信息

- **官方文档**: https://docs.api.jikan.moe/
- **Base URL**: `https://api.jikan.moe/v4`
- **请求限制**: 3 请求/秒，60 请求/分钟
- **返回类型**: 所有接口返回 `Result<T>` 类型

### 数据模型说明

- `JikanResponse<T>`: 单个数据响应，包含 `data` 字段
- `JikanPageResponse<T>`: 分页数据响应，包含 `data` 列表和 `pagination` 信息
- `Result<T>`: Kotlin Result 类型，用于错误处理

---

## Anime API - 动漫接口

**官方文档**: https://docs.api.jikan.moe/#tag/anime

### 基础信息查询

#### getAnimeById
- **路径**: `GET /anime/{id}`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
- **返回**: `Result<JikanResponse<Anime>>`
- **说明**: 获取单个动漫的基础信息

#### getAnimeFullById
- **路径**: `GET /anime/{id}/full`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
- **返回**: `Result<JikanResponse<Anime>>`
- **说明**: 获取单个动漫的完整信息（包含关系、主题曲等）

### 角色与工作人员

#### getAnimeCharacters
- **路径**: `GET /anime/{id}/characters`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
- **返回**: `Result<JikanResponse<List<AnimeCharacter>>>`
- **说明**: 获取动漫的角色列表

#### getAnimeStaff
- **路径**: `GET /anime/{id}/staff`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
- **返回**: `Result<JikanResponse<List<AnimeStaff>>>`
- **说明**: 获取动漫的工作人员列表

### 剧集信息

#### getAnimeEpisodes
- **路径**: `GET /anime/{id}/episodes`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
  - `page: Int?` - 页码（可选，从 1 开始）
- **返回**: `Result<JikanPageResponse<AnimeEpisode>>`
- **说明**: 获取动漫的剧集列表

#### getAnimeEpisodeById
- **路径**: `GET /anime/{id}/episodes/{episode}`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
  - `episode: Int` - 集数（必需）
- **返回**: `Result<JikanResponse<AnimeEpisode>>`
- **说明**: 获取动漫的单个剧集详细信息

### 社区内容

#### getAnimeNews
- **路径**: `GET /anime/{id}/news`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<AnimeNews>>`
- **说明**: 获取动漫的新闻列表

#### getAnimeForum
- **路径**: `GET /anime/{id}/forum`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
  - `filter: String?` - 过滤条件（可选）：`all`, `episode`, `other`
- **返回**: `Result<JikanResponse<List<ForumTopic>>>`
- **说明**: 获取动漫的论坛话题列表

#### getAnimeReviews
- **路径**: `GET /anime/{id}/reviews`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<AnimeReview>>`
- **说明**: 获取动漫的评论列表

### 多媒体内容

#### getAnimeVideos
- **路径**: `GET /anime/{id}/videos`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
- **返回**: `Result<JikanResponse<AnimeVideos>>`
- **说明**: 获取动漫的视频列表（预告、剧集、MV）

#### getAnimePictures
- **路径**: `GET /anime/{id}/pictures`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
- **返回**: `Result<JikanResponse<List<Picture>>>`
- **说明**: 获取动漫的图片列表

### 统计与推荐

#### getAnimeStatistics
- **路径**: `GET /anime/{id}/statistics`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
- **返回**: `Result<JikanResponse<AnimeStatistics>>`
- **说明**: 获取动漫的统计数据（观看人数、评分分布等）

#### getAnimeRecommendations
- **路径**: `GET /anime/{id}/recommendations`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
- **返回**: `Result<JikanResponse<List<Recommendation>>>`
- **说明**: 获取动漫的推荐列表

#### getAnimeUserUpdates
- **路径**: `GET /anime/{id}/userupdates`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<UserUpdate>>`
- **说明**: 获取动漫的用户更新列表

### 其他信息

#### getAnimeMoreInfo
- **路径**: `GET /anime/{id}/moreinfo`
- **参数**:
  - `id: Int` - MAL 动漫 ID（必需）
- **返回**: `Result<JikanResponse<String>>`
- **说明**: 获取动漫的更多信息文本

### 搜索与排行

#### searchAnime
- **路径**: `GET /anime`
- **参数**:
  - `query: String?` - 搜索关键词（可选）
  - `page: Int?` - 页码（可选，从 1 开始）
  - `limit: Int?` - 每页数量（可选，最大 25）
  - `type: String?` - 动漫类型（可选）：`tv`, `movie`, `ova`, `special`, `ona`, `music`
  - `score: Double?` - 最低评分（可选）
  - `status: String?` - 状态（可选）：`airing`, `complete`, `upcoming`
  - `rating: String?` - 分级（可选）：`g`, `pg`, `pg13`, `r17`, `r`, `rx`
  - `sfw: Boolean?` - 是否仅显示安全内容（可选）
  - `genres: String?` - 类型 ID 列表，逗号分隔（可选）
  - `orderBy: String?` - 排序字段（可选）
  - `sort: String?` - 排序方向（可选）：`asc`, `desc`
- **返回**: `Result<JikanPageResponse<Anime>>`
- **说明**: 搜索动漫

#### getTopAnime
- **路径**: `GET /top/anime`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
  - `filter: AnimeFilter?` - 过滤条件（可选）：`BY_POPULARITY`, `AIRING`, `UPCOMING`, `FAVORITE`
- **返回**: `Result<JikanPageResponse<Anime>>`
- **说明**: 获取 Top 动漫排行榜

---

## Manga API - 漫画接口

**官方文档**: https://docs.api.jikan.moe/#tag/manga

### 基础信息查询

#### getMangaById
- **路径**: `GET /manga/{id}`
- **参数**:
  - `id: Int` - MAL 漫画 ID（必需）
- **返回**: `Result<JikanResponse<Manga>>`
- **说明**: 获取单个漫画的基础信息

#### getMangaFullById
- **路径**: `GET /manga/{id}/full`
- **参数**:
  - `id: Int` - MAL 漫画 ID（必需）
- **返回**: `Result<JikanResponse<Manga>>`
- **说明**: 获取单个漫画的完整信息

### 角色信息

#### getMangaCharacters
- **路径**: `GET /manga/{id}/characters`
- **参数**:
  - `id: Int` - MAL 漫画 ID（必需）
- **返回**: `Result<JikanResponse<List<MangaCharacter>>>`
- **说明**: 获取漫画的角色列表

### 社区内容

#### getMangaNews
- **路径**: `GET /manga/{id}/news`
- **参数**:
  - `id: Int` - MAL 漫画 ID（必需）
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<MangaNews>>`
- **说明**: 获取漫画的新闻列表

#### getMangaForum
- **路径**: `GET /manga/{id}/forum`
- **参数**:
  - `id: Int` - MAL 漫画 ID（必需）
  - `filter: String?` - 过滤条件（可选）
- **返回**: `Result<JikanResponse<List<ForumTopic>>>`
- **说明**: 获取漫画的论坛话题列表

#### getMangaReviews
- **路径**: `GET /manga/{id}/reviews`
- **参数**:
  - `id: Int` - MAL 漫画 ID（必需）
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<MangaReview>>`
- **说明**: 获取漫画的评论列表

### 多媒体与统计

#### getMangaPictures
- **路径**: `GET /manga/{id}/pictures`
- **参数**:
  - `id: Int` - MAL 漫画 ID（必需）
- **返回**: `Result<JikanResponse<List<MangaPicture>>>`
- **说明**: 获取漫画的图片列表

#### getMangaStatistics
- **路径**: `GET /manga/{id}/statistics`
- **参数**:
  - `id: Int` - MAL 漫画 ID（必需）
- **返回**: `Result<JikanResponse<MangaStatistics>>`
- **说明**: 获取漫画的统计数据

### 其他信息

#### getMangaMoreInfo
- **路径**: `GET /manga/{id}/moreinfo`
- **参数**:
  - `id: Int` - MAL 漫画 ID（必需）
- **返回**: `Result<JikanResponse<String>>`
- **说明**: 获取漫画的更多信息文本

#### getMangaRecommendations
- **路径**: `GET /manga/{id}/recommendations`
- **参数**:
  - `id: Int` - MAL 漫画 ID（必需）
- **返回**: `Result<JikanResponse<List<Recommendation>>>`
- **说明**: 获取漫画的推荐列表

#### getMangaUserUpdates
- **路径**: `GET /manga/{id}/userupdates`
- **参数**:
  - `id: Int` - MAL 漫画 ID（必需）
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<UserUpdate>>`
- **说明**: 获取漫画的用户更新列表

### 搜索与排行

#### searchManga
- **路径**: `GET /manga`
- **参数**:
  - `query: String?` - 搜索关键词（可选）
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
  - `type: String?` - 漫画类型（可选）：`manga`, `novel`, `lightnovel`, `oneshot`, `doujin`, `manhwa`, `manhua`
  - `score: Double?` - 最低评分（可选）
  - `status: String?` - 状态（可选）：`publishing`, `complete`, `upcoming`
  - `sfw: Boolean?` - 是否仅显示安全内容（可选）
  - `genres: String?` - 类型 ID 列表（可选）
  - `orderBy: String?` - 排序字段（可选）
  - `sort: String?` - 排序方向（可选）
- **返回**: `Result<JikanPageResponse<Manga>>`
- **说明**: 搜索漫画

#### getTopManga
- **路径**: `GET /top/manga`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
  - `filter: MangaFilter?` - 过滤条件（可选）：`BY_POPULARITY`, `PUBLISHING`, `UPCOMING`, `FAVORITE`
- **返回**: `Result<JikanPageResponse<Manga>>`
- **说明**: 获取 Top 漫画排行榜

---

## Character API - 角色接口

**官方文档**: https://docs.api.jikan.moe/#tag/characters

### 基础信息查询

#### getCharacterById
- **路径**: `GET /characters/{id}`
- **参数**:
  - `id: Int` - MAL 角色 ID（必需）
- **返回**: `Result<JikanResponse<Character>>`
- **说明**: 获取角色的基础信息

#### getCharacterFullById
- **路径**: `GET /characters/{id}/full`
- **参数**:
  - `id: Int` - MAL 角色 ID（必需）
- **返回**: `Result<JikanResponse<Character>>`
- **说明**: 获取角色的完整信息（包含动漫、漫画、声优）

#### getCharacterPictures
- **路径**: `GET /characters/{id}/pictures`
- **参数**:
  - `id: Int` - MAL 角色 ID（必需）
- **返回**: `Result<JikanResponse<List<CharacterPicture>>>`
- **说明**: 获取角色的图片列表

### 搜索与排行

#### searchCharacters
- **路径**: `GET /characters`
- **参数**:
  - `query: String?` - 搜索关键词（可选）
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
  - `orderBy: String?` - 排序字段（可选）：`mal_id`, `name`, `favorites`
  - `sort: String?` - 排序方向（可选）：`asc`, `desc`
- **返回**: `Result<JikanPageResponse<Character>>`
- **说明**: 搜索角色

#### getTopCharacters
- **路径**: `GET /top/characters`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
- **返回**: `Result<JikanPageResponse<Character>>`
- **说明**: 获取 Top 角色排行榜

---

## People API - 人物接口

**官方文档**: https://docs.api.jikan.moe/#tag/people

### 基础信息查询

#### getPersonById
- **路径**: `GET /people/{id}`
- **参数**:
  - `id: Int` - MAL 人物 ID（必需）
- **返回**: `Result<JikanResponse<Person>>`
- **说明**: 获取人物的基础信息

#### getPersonFullById
- **路径**: `GET /people/{id}/full`
- **参数**:
  - `id: Int` - MAL 人物 ID（必需）
- **返回**: `Result<JikanResponse<Person>>`
- **说明**: 获取人物的完整信息

#### getPersonPictures
- **路径**: `GET /people/{id}/pictures`
- **参数**:
  - `id: Int` - MAL 人物 ID（必需）
- **返回**: `Result<JikanResponse<List<PersonPicture>>>`
- **说明**: 获取人物的图片列表

### 搜索与排行

#### searchPeople
- **路径**: `GET /people`
- **参数**:
  - `query: String?` - 搜索关键词（可选）
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
  - `orderBy: String?` - 排序字段（可选）：`mal_id`, `name`, `birthday`, `favorites`
  - `sort: String?` - 排序方向（可选）
- **返回**: `Result<JikanPageResponse<Person>>`
- **说明**: 搜索人物

#### getTopPeople
- **路径**: `GET /top/people`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
- **返回**: `Result<JikanPageResponse<Person>>`
- **说明**: 获取 Top 人物排行榜

---

## Season API - 季度接口

**官方文档**: https://docs.api.jikan.moe/#tag/seasons

### 季度查询

#### getSeasonAnime
- **路径**: `GET /seasons/{year}/{season}`
- **参数**:
  - `year: Int` - 年份（必需），例如 2023
  - `season: String` - 季度（必需）：`winter`, `spring`, `summer`, `fall`
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
- **返回**: `Result<JikanPageResponse<Anime>>`
- **说明**: 获取指定季度的动漫列表

#### getCurrentSeasonAnime
- **路径**: `GET /seasons/now`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
- **返回**: `Result<JikanPageResponse<Anime>>`
- **说明**: 获取当前季度的动漫列表

#### getUpcomingSeasonAnime
- **路径**: `GET /seasons/upcoming`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
- **返回**: `Result<JikanPageResponse<Anime>>`
- **说明**: 获取即将播出的季度动漫列表

#### getAllSeasons
- **路径**: `GET /seasons`
- **参数**: 无
- **返回**: `Result<JikanResponse<List<Season>>>`
- **说明**: 获取所有可用的季度列表

---

## Producer API - 制作公司接口

**官方文档**: https://docs.api.jikan.moe/#tag/producers

### 基础信息查询

#### getProducerById
- **路径**: `GET /producers/{id}`
- **参数**:
  - `id: Int` - MAL 制作公司 ID（必需）
- **返回**: `Result<JikanResponse<Producer>>`
- **说明**: 获取制作公司的基础信息

#### getProducerFullById
- **路径**: `GET /producers/{id}/full`
- **参数**:
  - `id: Int` - MAL 制作公司 ID（必需）
- **返回**: `Result<JikanResponse<Producer>>`
- **说明**: 获取制作公司的完整信息

### 搜索

#### searchProducers
- **路径**: `GET /producers`
- **参数**:
  - `query: String?` - 搜索关键词（可选）
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
  - `orderBy: String?` - 排序字段（可选）：`mal_id`, `name`, `count`, `favorites`, `established`
  - `sort: String?` - 排序方向（可选）
- **返回**: `Result<JikanPageResponse<Producer>>`
- **说明**: 搜索制作公司

---

## Magazine API - 杂志接口

**官方文档**: https://docs.api.jikan.moe/#tag/magazines

### 搜索

#### searchMagazines
- **路径**: `GET /magazines`
- **参数**:
  - `query: String?` - 搜索关键词（可选）
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
  - `orderBy: String?` - 排序字段（可选）：`mal_id`, `name`, `count`
  - `sort: String?` - 排序方向（可选）
- **返回**: `Result<JikanPageResponse<Magazine>>`
- **说明**: 搜索杂志/出版社

---

## Club API - 俱乐部接口

**官方文档**: https://docs.api.jikan.moe/#tag/clubs

### 基础信息查询

#### getClubById
- **路径**: `GET /clubs/{id}`
- **参数**:
  - `id: Int` - MAL 俱乐部 ID（必需）
- **返回**: `Result<JikanResponse<Club>>`
- **说明**: 获取俱乐部的基础信息

#### getClubMembers
- **路径**: `GET /clubs/{id}/members`
- **参数**:
  - `id: Int` - MAL 俱乐部 ID（必需）
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<ClubMember>>`
- **说明**: 获取俱乐部的成员列表

#### getClubStaff
- **路径**: `GET /clubs/{id}/staff`
- **参数**:
  - `id: Int` - MAL 俱乐部 ID（必需）
- **返回**: `Result<JikanResponse<List<ClubStaff>>>`
- **说明**: 获取俱乐部的工作人员列表

#### getClubRelations
- **路径**: `GET /clubs/{id}/relations`
- **参数**:
  - `id: Int` - MAL 俱乐部 ID（必需）
- **返回**: `Result<JikanResponse<ClubRelations>>`
- **说明**: 获取俱乐部的关系（相关动漫、漫画、角色）

### 搜索

#### searchClubs
- **路径**: `GET /clubs`
- **参数**:
  - `query: String?` - 搜索关键词（可选）
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
  - `type: String?` - 类型（可选）：`public`, `private`, `secret`
  - `category: String?` - 分类（可选）：`anime`, `manga`, `actors_and_artists`, `characters` 等
  - `orderBy: String?` - 排序字段（可选）：`mal_id`, `name`, `members_count`, `pictures_count`, `created`
  - `sort: String?` - 排序方向（可选）
- **返回**: `Result<JikanPageResponse<Club>>`
- **说明**: 搜索俱乐部

---

## User API - 用户接口

**官方文档**: https://docs.api.jikan.moe/#tag/users

### 基础信息查询

#### getUserByUsername
- **路径**: `GET /users/{username}`
- **参数**:
  - `username: String` - MAL 用户名（必需）
- **返回**: `Result<JikanResponse<User>>`
- **说明**: 获取用户的基础信息

#### getUserFullProfile
- **路径**: `GET /users/{username}/full`
- **参数**:
  - `username: String` - MAL 用户名（必需）
- **返回**: `Result<JikanResponse<User>>`
- **说明**: 获取用户的完整资料

#### getUserStatistics
- **路径**: `GET /users/{username}/statistics`
- **参数**:
  - `username: String` - MAL 用户名（必需）
- **返回**: `Result<JikanResponse<UserStatistics>>`
- **说明**: 获取用户的统计信息

#### getUserFavorites
- **路径**: `GET /users/{username}/favorites`
- **参数**:
  - `username: String` - MAL 用户名（必需）
- **返回**: `Result<JikanResponse<UserFavorites>>`
- **说明**: 获取用户的收藏

#### getUserFriends
- **路径**: `GET /users/{username}/friends`
- **参数**:
  - `username: String` - MAL 用户名（必需）
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<UserFriend>>`
- **说明**: 获取用户的好友列表

#### getUserHistory
- **路径**: `GET /users/{username}/history`
- **参数**:
  - `username: String` - MAL 用户名（必需）
  - `type: String?` - 类型（可选）：`anime`, `manga`
- **返回**: `Result<JikanResponse<List<UserHistory>>>`
- **说明**: 获取用户的历史记录

### 搜索

#### searchUsers
- **路径**: `GET /users`
- **参数**:
  - `query: String?` - 搜索关键词（可选）
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
  - `gender: String?` - 性别（可选）：`any`, `male`, `female`, `nonbinary`
  - `location: String?` - 所在地（可选）
  - `maxAge: Int?` - 最大年龄（可选）
  - `minAge: Int?` - 最小年龄（可选）
- **返回**: `Result<JikanPageResponse<User>>`
- **说明**: 搜索用户

---

## Watch API - 观看推荐接口

**官方文档**: https://docs.api.jikan.moe/#tag/watch

### 最新视频

#### getRecentPromoVideos
- **路径**: `GET /watch/promos`
- **参数**:
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<RecentPromoVideo>>`
- **说明**: 获取最新的宣传视频列表

#### getRecentEpisodeVideos
- **路径**: `GET /watch/episodes`
- **参数**:
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<RecentEpisodeVideo>>`
- **说明**: 获取最新的剧集视频列表

### 热门视频

#### getPopularPromoVideos
- **路径**: `GET /watch/promos/popular`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
- **返回**: `Result<JikanPageResponse<RecentPromoVideo>>`
- **说明**: 获取热门宣传视频列表

#### getPopularEpisodeVideos
- **路径**: `GET /watch/episodes/popular`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
- **返回**: `Result<JikanPageResponse<RecentEpisodeVideo>>`
- **说明**: 获取热门剧集视频列表

---

## Genres API - 类型接口

**官方文档**: https://docs.api.jikan.moe/#tag/genres

### 类型查询

#### getAnimeGenres
- **路径**: `GET /genres/anime`
- **参数**:
  - `filter: String?` - 过滤条件（可选）
- **返回**: `Result<JikanResponse<List<Genre>>>`
- **说明**: 获取动漫类型列表

#### getMangaGenres
- **路径**: `GET /genres/manga`
- **参数**:
  - `filter: String?` - 过滤条件（可选）
- **返回**: `Result<JikanResponse<List<Genre>>>`
- **说明**: 获取漫画类型列表

---

## Random API - 随机接口

**官方文档**: https://docs.api.jikan.moe/#tag/random

### 随机资源获取

#### getRandomAnime
- **路径**: `GET /random/anime`
- **参数**: 无
- **返回**: `Result<JikanResponse<Anime>>`
- **说明**: 获取随机动漫

#### getRandomManga
- **路径**: `GET /random/manga`
- **参数**: 无
- **返回**: `Result<JikanResponse<Manga>>`
- **说明**: 获取随机漫画

#### getRandomCharacter
- **路径**: `GET /random/characters`
- **参数**: 无
- **返回**: `Result<JikanResponse<Character>>`
- **说明**: 获取随机角色

#### getRandomPerson
- **路径**: `GET /random/people`
- **参数**: 无
- **返回**: `Result<JikanResponse<Person>>`
- **说明**: 获取随机人物

#### getRandomUser
- **路径**: `GET /random/users`
- **参数**: 无
- **返回**: `Result<JikanResponse<User>>`
- **说明**: 获取随机用户

---

## Recommendations API - 推荐接口

**官方文档**: https://docs.api.jikan.moe/#tag/recommendations

### 最新推荐

#### getRecentAnimeRecommendations
- **路径**: `GET /recommendations/anime`
- **参数**:
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<RecentRecommendation>>`
- **说明**: 获取最新的动漫推荐列表

#### getRecentMangaRecommendations
- **路径**: `GET /recommendations/manga`
- **参数**:
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<RecentRecommendation>>`
- **说明**: 获取最新的漫画推荐列表

---

## Reviews API - 评论接口

**官方文档**: https://docs.api.jikan.moe/#tag/reviews

### 最新评论

#### getRecentAnimeReviews
- **路径**: `GET /reviews/anime`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `preliminary: Boolean?` - 是否包含预览评论（可选）
  - `spoiler: Boolean?` - 是否包含剧透评论（可选）
- **返回**: `Result<JikanPageResponse<RecentReview>>`
- **说明**: 获取最新的动漫评论列表

#### getRecentMangaReviews
- **路径**: `GET /reviews/manga`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `preliminary: Boolean?` - 是否包含预览评论（可选）
  - `spoiler: Boolean?` - 是否包含剧透评论（可选）
- **返回**: `Result<JikanPageResponse<RecentReview>>`
- **说明**: 获取最新的漫画评论列表

---

## Schedules API - 时间表接口

**官方文档**: https://docs.api.jikan.moe/#tag/schedules

### 播放时间表

#### getSchedules
- **路径**: `GET /schedules`
- **参数**:
  - `filter: String?` - 过滤条件（可选）：星期几，例如 `monday`, `tuesday` 等
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
- **返回**: `Result<JikanPageResponse<Anime>>`
- **说明**: 获取动漫播放时间表

---

## Top API - 排行榜接口

**官方文档**: https://docs.api.jikan.moe/#tag/top

### 排行榜查询

#### getTopAnime
- **路径**: `GET /top/anime`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
  - `filter: String?` - 过滤条件（可选）
  - `type: String?` - 动漫类型（可选）
- **返回**: `Result<JikanPageResponse<Anime>>`
- **说明**: 获取动漫排行榜

#### getTopManga
- **路径**: `GET /top/manga`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
  - `filter: String?` - 过滤条件（可选）
  - `type: String?` - 漫画类型（可选）
- **返回**: `Result<JikanPageResponse<Manga>>`
- **说明**: 获取漫画排行榜

#### getTopCharacters
- **路径**: `GET /top/characters`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
- **返回**: `Result<JikanPageResponse<Character>>`
- **说明**: 获取角色排行榜

#### getTopPeople
- **路径**: `GET /top/people`
- **参数**:
  - `page: Int?` - 页码（可选）
  - `limit: Int?` - 每页数量（可选）
- **返回**: `Result<JikanPageResponse<Person>>`
- **说明**: 获取人物排行榜

#### getTopReviews
- **路径**: `GET /top/reviews`
- **参数**:
  - `page: Int?` - 页码（可选）
- **返回**: `Result<JikanPageResponse<RecentReview>>`
- **说明**: 获取评论排行榜

---

## 附录

### 枚举类型

#### AnimeFilter
- `BY_POPULARITY` - 按人气排序
- `AIRING` - 正在播出
- `UPCOMING` - 即将播出
- `FAVORITE` - 按收藏数排序

#### MangaFilter
- `BY_POPULARITY` - 按人气排序
- `PUBLISHING` - 连载中
- `UPCOMING` - 即将发布
- `FAVORITE` - 按收藏数排序

### 参考资源

- **Jikan 官方文档**: https://docs.api.jikan.moe/
- **MyAnimeList**: https://myanimelist.net/

---

**文档版本**: 2.0.0  
**最后更新**: 2025-01-01  
**API 版本**: v4
