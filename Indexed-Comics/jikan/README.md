# Jikan API 模块

本模块实现了对 [Jikan API v4](https://docs.api.jikan.moe/) 的完整封装。Jikan 是一个非官方的 MyAnimeList API，提供对动漫、漫画、角色等数据的访问。

## 基础信息

- **Base URL**: `https://api.jikan.moe/v4/`
- **协议**: HTTPS
- **数据格式**: JSON
- **认证**: 无需认证
- **限流**: 请求需遵守 API 限流规则

## API 接口列表

### 1. Anime（动漫）

#### 1.1 获取动漫详情
- **接口**: `GET /anime/{id}`
- **描述**: 根据 MAL ID 获取动漫详细信息
- **参数**: `id` - MyAnimeList 动漫 ID

#### 1.2 获取动漫角色和声优
- **接口**: `GET /anime/{id}/characters`
- **描述**: 获取指定动漫的角色及声优信息

#### 1.3 获取动漫工作人员
- **接口**: `GET /anime/{id}/staff`
- **描述**: 获取指定动漫的制作人员信息

#### 1.4 获取动漫剧集列表
- **接口**: `GET /anime/{id}/episodes`
- **描述**: 获取指定动漫的分集信息
- **查询参数**: `page` - 页码

#### 1.5 获取动漫剧集详情
- **接口**: `GET /anime/{id}/episodes/{episode}`
- **描述**: 获取指定动漫特定集数的详细信息

#### 1.6 获取动漫新闻
- **接口**: `GET /anime/{id}/news`
- **描述**: 获取指定动漫的相关新闻
- **查询参数**: `page` - 页码

#### 1.7 获取动漫论坛话题
- **接口**: `GET /anime/{id}/forum`
- **描述**: 获取指定动漫的论坛讨论话题
- **查询参数**: `filter` - 话题类型筛选

#### 1.8 获取动漫视频
- **接口**: `GET /anime/{id}/videos`
- **描述**: 获取指定动漫的宣传视频、PV等

#### 1.9 获取动漫图片
- **接口**: `GET /anime/{id}/pictures`
- **描述**: 获取指定动漫的图片集

#### 1.10 获取动漫统计信息
- **接口**: `GET /anime/{id}/statistics`
- **描述**: 获取指定动漫的观看统计数据

#### 1.11 获取动漫更多信息
- **接口**: `GET /anime/{id}/moreinfo`
- **描述**: 获取指定动漫的额外信息

#### 1.12 获取动漫推荐
- **接口**: `GET /anime/{id}/recommendations`
- **描述**: 获取与指定动漫相似的推荐作品

#### 1.13 获取动漫用户更新
- **接口**: `GET /anime/{id}/userupdates`
- **描述**: 获取用户对该动漫的最新更新
- **查询参数**: `page` - 页码

#### 1.14 获取动漫评论
- **接口**: `GET /anime/{id}/reviews`
- **描述**: 获取指定动漫的用户评论
- **查询参数**: `page` - 页码, `preliminary` - 是否包含初步评论, `spoiler` - 是否包含剧透

#### 1.15 获取动漫关系
- **接口**: `GET /anime/{id}/relations`
- **描述**: 获取指定动漫的相关作品（续集、前传等）

#### 1.16 获取动漫主题
- **接口**: `GET /anime/{id}/themes`
- **描述**: 获取指定动漫的OP/ED主题曲信息

#### 1.17 获取动漫外部链接
- **接口**: `GET /anime/{id}/external`
- **描述**: 获取指定动漫的外部网站链接

#### 1.18 获取动漫流媒体链接
- **接口**: `GET /anime/{id}/streaming`
- **描述**: 获取指定动漫的在线观看平台链接

#### 1.19 获取完整动漫信息
- **接口**: `GET /anime/{id}/full`
- **描述**: 一次性获取动漫的所有可用信息

#### 1.20 搜索动漫
- **接口**: `GET /anime`
- **描述**: 根据条件搜索动漫
- **查询参数**: 
  - `q` - 搜索关键词
  - `type` - 类型（tv, movie, ova, special, ona, music）
  - `score` - 最低评分
  - `status` - 状态（airing, complete, upcoming）
  - `rating` - 年龄分级
  - `sfw` - 是否安全内容
  - `genres` - 类型ID
  - `order_by` - 排序字段
  - `sort` - 排序方向（asc, desc）
  - `page` - 页码
  - `limit` - 每页数量

### 2. Manga（漫画）

#### 2.1 获取漫画详情
- **接口**: `GET /manga/{id}`
- **描述**: 根据 MAL ID 获取漫画详细信息

#### 2.2 获取漫画角色
- **接口**: `GET /manga/{id}/characters`
- **描述**: 获取指定漫画的角色信息

#### 2.3 获取漫画新闻
- **接口**: `GET /manga/{id}/news`
- **描述**: 获取指定漫画的相关新闻
- **查询参数**: `page` - 页码

#### 2.4 获取漫画论坛话题
- **接口**: `GET /manga/{id}/forum`
- **描述**: 获取指定漫画的论坛讨论话题

#### 2.5 获取漫画图片
- **接口**: `GET /manga/{id}/pictures`
- **描述**: 获取指定漫画的图片集

#### 2.6 获取漫画统计信息
- **接口**: `GET /manga/{id}/statistics`
- **描述**: 获取指定漫画的阅读统计数据

#### 2.7 获取漫画更多信息
- **接口**: `GET /manga/{id}/moreinfo`
- **描述**: 获取指定漫画的额外信息

#### 2.8 获取漫画推荐
- **接口**: `GET /manga/{id}/recommendations`
- **描述**: 获取与指定漫画相似的推荐作品

#### 2.9 获取漫画用户更新
- **接口**: `GET /manga/{id}/userupdates`
- **描述**: 获取用户对该漫画的最新更新
- **查询参数**: `page` - 页码

#### 2.10 获取漫画评论
- **接口**: `GET /manga/{id}/reviews`
- **描述**: 获取指定漫画的用户评论
- **查询参数**: `page` - 页码, `preliminary` - 是否包含初步评论, `spoiler` - 是否包含剧透

#### 2.11 获取漫画关系
- **接口**: `GET /manga/{id}/relations`
- **描述**: 获取指定漫画的相关作品

#### 2.12 获取漫画外部链接
- **接口**: `GET /manga/{id}/external`
- **描述**: 获取指定漫画的外部网站链接

#### 2.13 获取完整漫画信息
- **接口**: `GET /manga/{id}/full`
- **描述**: 一次性获取漫画的所有可用信息

#### 2.14 搜索漫画
- **接口**: `GET /manga`
- **描述**: 根据条件搜索漫画
- **查询参数**: 类似动漫搜索参数

### 3. Characters（角色）

#### 3.1 获取角色详情
- **接口**: `GET /characters/{id}`
- **描述**: 根据 ID 获取角色详细信息

#### 3.2 获取角色动漫出演列表
- **接口**: `GET /characters/{id}/anime`
- **描述**: 获取角色出演的动漫列表

#### 3.3 获取角色漫画出现列表
- **接口**: `GET /characters/{id}/manga`
- **描述**: 获取角色出现的漫画列表

#### 3.4 获取角色声优
- **接口**: `GET /characters/{id}/voices`
- **描述**: 获取为该角色配音的声优信息

#### 3.5 获取角色图片
- **接口**: `GET /characters/{id}/pictures`
- **描述**: 获取角色的图片集

#### 3.6 获取完整角色信息
- **接口**: `GET /characters/{id}/full`
- **描述**: 一次性获取角色的所有可用信息

#### 3.7 搜索角色
- **接口**: `GET /characters`
- **描述**: 根据条件搜索角色
- **查询参数**: `q` - 搜索关键词, `page` - 页码, `limit` - 每页数量

### 4. People（人物/声优）

#### 4.1 获取人物详情
- **接口**: `GET /people/{id}`
- **描述**: 根据 ID 获取人物（声优/制作人员）详细信息

#### 4.2 获取人物动漫作品
- **接口**: `GET /people/{id}/anime`
- **描述**: 获取人物参与的动漫作品

#### 4.3 获取人物配音角色
- **接口**: `GET /people/{id}/voices`
- **描述**: 获取声优配音的角色列表

#### 4.4 获取人物漫画作品
- **接口**: `GET /people/{id}/manga`
- **描述**: 获取人物参与的漫画作品

#### 4.5 获取人物图片
- **接口**: `GET /people/{id}/pictures`
- **描述**: 获取人物的图片集

#### 4.6 获取完整人物信息
- **接口**: `GET /people/{id}/full`
- **描述**: 一次性获取人物的所有可用信息

#### 4.7 搜索人物
- **接口**: `GET /people`
- **描述**: 根据条件搜索人物
- **查询参数**: `q` - 搜索关键词, `page` - 页码, `limit` - 每页数量

### 5. Seasons（季度动漫）

#### 5.1 获取指定季度动漫列表
- **接口**: `GET /seasons/{year}/{season}`
- **描述**: 获取指定年份和季度的动漫列表
- **参数**: `year` - 年份, `season` - 季度（winter, spring, summer, fall）
- **查询参数**: `page` - 页码, `limit` - 每页数量

#### 5.2 获取当前季度动漫
- **接口**: `GET /seasons/now`
- **描述**: 获取当前播放季度的动漫列表
- **查询参数**: `page` - 页码, `limit` - 每页数量

#### 5.3 获取即将播出动漫
- **接口**: `GET /seasons/upcoming`
- **描述**: 获取即将播出的动漫列表
- **查询参数**: `page` - 页码, `limit` - 每页数量

#### 5.4 获取所有可用季度列表
- **接口**: `GET /seasons`
- **描述**: 获取 MyAnimeList 上所有有动漫数据的季度列表

### 6. Schedules（播放时间表）

#### 6.1 获取动漫播放时间表
- **接口**: `GET /schedules`
- **描述**: 获取当前季度的动漫播放时间表
- **查询参数**: 
  - `filter` - 星期筛选（monday, tuesday, wednesday, thursday, friday, saturday, sunday, unknown, other）
  - `page` - 页码
  - `limit` - 每页数量

### 7. Top（排行榜）

#### 7.1 获取动漫排行榜
- **接口**: `GET /top/anime`
- **描述**: 获取动漫排行榜
- **查询参数**: 
  - `type` - 类型筛选（tv, movie, ova, special, ona, music）
  - `filter` - 排序方式（airing, upcoming, bypopularity, favorite）
  - `page` - 页码
  - `limit` - 每页数量

#### 7.2 获取漫画排行榜
- **接口**: `GET /top/manga`
- **描述**: 获取漫画排行榜
- **查询参数**: 类似动漫排行榜参数

#### 7.3 获取角色排行榜
- **接口**: `GET /top/characters`
- **描述**: 获取最受欢迎角色排行榜
- **查询参数**: `page` - 页码, `limit` - 每页数量

#### 7.4 获取人物排行榜
- **接口**: `GET /top/people`
- **描述**: 获取最受欢迎人物排行榜
- **查询参数**: `page` - 页码, `limit` - 每页数量

#### 7.5 获取评论排行榜
- **接口**: `GET /top/reviews`
- **描述**: 获取最佳评论排行榜
- **查询参数**: `page` - 页码, `limit` - 每页数量

### 8. Genres（类型/标签）

#### 8.1 获取动漫类型列表
- **接口**: `GET /genres/anime`
- **描述**: 获取所有动漫类型/标签列表
- **查询参数**: `filter` - 筛选类型（genres, explicit_genres, themes, demographics）

#### 8.2 获取漫画类型列表
- **接口**: `GET /genres/manga`
- **描述**: 获取所有漫画类型/标签列表
- **查询参数**: `filter` - 筛选类型（genres, explicit_genres, themes, demographics）

### 9. Producers（制作公司）

#### 9.1 获取制作公司列表
- **接口**: `GET /producers`
- **描述**: 获取制作公司列表
- **查询参数**: `page` - 页码, `limit` - 每页数量

#### 9.2 获取制作公司详情
- **接口**: `GET /producers/{id}`
- **描述**: 根据 ID 获取制作公司详细信息

#### 9.3 获取完整制作公司信息
- **接口**: `GET /producers/{id}/full`
- **描述**: 获取制作公司完整信息

#### 9.4 获取制作公司外部链接
- **接口**: `GET /producers/{id}/external`
- **描述**: 获取制作公司的外部链接

### 10. Magazines（杂志/出版社）

#### 10.1 获取杂志列表
- **接口**: `GET /magazines`
- **描述**: 获取漫画杂志/出版社列表
- **查询参数**: `page` - 页码, `limit` - 每页数量

#### 10.2 获取杂志详情
- **接口**: `GET /magazines/{id}`
- **描述**: 根据 ID 获取杂志详细信息

### 11. Clubs（俱乐部）

#### 11.1 获取俱乐部列表
- **接口**: `GET /clubs`
- **描述**: 获取俱乐部列表
- **查询参数**: 
  - `q` - 搜索关键词
  - `type` - 俱乐部类型
  - `category` - 俱乐部分类
  - `page` - 页码
  - `limit` - 每页数量

#### 11.2 获取俱乐部详情
- **接口**: `GET /clubs/{id}`
- **描述**: 根据 ID 获取俱乐部详细信息

#### 11.3 获取俱乐部成员
- **接口**: `GET /clubs/{id}/members`
- **描述**: 获取俱乐部成员列表
- **查询参数**: `page` - 页码

#### 11.4 获取俱乐部工作人员
- **接口**: `GET /clubs/{id}/staff`
- **描述**: 获取俱乐部管理人员列表

#### 11.5 获取俱乐部关系
- **接口**: `GET /clubs/{id}/relations`
- **描述**: 获取俱乐部相关的动漫、漫画、角色信息

### 12. Users（用户）

#### 12.1 获取用户完整信息
- **接口**: `GET /users/{username}/full`
- **描述**: 获取指定用户的完整信息

#### 12.2 获取用户基本信息
- **接口**: `GET /users/{username}`
- **描述**: 获取指定用户的基本信息

#### 12.3 获取用户统计
- **接口**: `GET /users/{username}/statistics`
- **描述**: 获取用户的观看统计数据

#### 12.4 获取用户收藏
- **接口**: `GET /users/{username}/favorites`
- **描述**: 获取用户收藏的动漫、漫画、角色等

#### 12.5 获取用户更新
- **接口**: `GET /users/{username}/updates`
- **描述**: 获取用户的最近更新

#### 12.6 获取用户关于
- **接口**: `GET /users/{username}/about`
- **描述**: 获取用户的个人介绍

#### 12.7 获取用户评论
- **接口**: `GET /users/{username}/reviews`
- **描述**: 获取用户发表的评论
- **查询参数**: `page` - 页码

#### 12.8 获取用户推荐
- **接口**: `GET /users/{username}/recommendations`
- **描述**: 获取用户发表的推荐

#### 12.9 获取用户俱乐部
- **接口**: `GET /users/{username}/clubs`
- **描述**: 获取用户加入的俱乐部
- **查询参数**: `page` - 页码

#### 12.10 获取用户外部链接
- **接口**: `GET /users/{username}/external`
- **描述**: 获取用户的外部社交链接

#### 12.11 获取用户动漫列表
- **接口**: `GET /users/{username}/animelist`
- **描述**: 获取用户的动漫列表

#### 12.12 获取用户漫画列表
- **接口**: `GET /users/{username}/mangalist`
- **描述**: 获取用户的漫画列表

#### 12.13 获取用户好友
- **接口**: `GET /users/{username}/friends`
- **描述**: 获取用户的好友列表
- **查询参数**: `page` - 页码

#### 12.14 获取用户历史
- **接口**: `GET /users/{username}/history`
- **描述**: 获取用户的观看历史
- **查询参数**: `type` - 类型（anime, manga）

#### 12.15 搜索用户
- **接口**: `GET /users`
- **描述**: 根据条件搜索用户
- **查询参数**: `q` - 搜索关键词, `page` - 页码, `limit` - 每页数量

### 13. Reviews（评论）

#### 13.1 获取最新评论
- **接口**: `GET /reviews/anime`
- **描述**: 获取最新的动漫评论
- **查询参数**: `page` - 页码

#### 13.2 获取最新漫画评论
- **接口**: `GET /reviews/manga`
- **描述**: 获取最新的漫画评论
- **查询参数**: `page` - 页码

### 14. Recommendations（推荐）

#### 14.1 获取最新动漫推荐
- **接口**: `GET /recommendations/anime`
- **描述**: 获取用户发布的最新动漫推荐
- **查询参数**: `page` - 页码

#### 14.2 获取最新漫画推荐
- **接口**: `GET /recommendations/manga`
- **描述**: 获取用户发布的最新漫画推荐
- **查询参数**: `page` - 页码

### 15. Watch（在线观看）

#### 15.1 获取最近添加的宣传视频
- **接口**: `GET /watch/promos`
- **描述**: 获取最近添加的动漫宣传视频

#### 15.2 获取最近添加的剧集视频
- **接口**: `GET /watch/episodes`
- **描述**: 获取最近添加的动漫剧集视频

#### 15.3 获取热门宣传视频
- **接口**: `GET /watch/promos/popular`
- **描述**: 获取热门的动漫宣传视频

### 16. Random（随机）

#### 16.1 获取随机动漫
- **接口**: `GET /random/anime`
- **描述**: 随机获取一部动漫

#### 16.2 获取随机漫画
- **接口**: `GET /random/manga`
- **描述**: 随机获取一部漫画

#### 16.3 获取随机角色
- **接口**: `GET /random/characters`
- **描述**: 随机获取一个角色

#### 16.4 获取随机人物
- **接口**: `GET /random/people`
- **描述**: 随机获取一个人物

#### 16.5 获取随机用户
- **接口**: `GET /random/users`
- **描述**: 随机获取一个用户

## 使用说明

### 依赖配置

本模块使用以下库：
- **Retrofit 3.0.0** - 网络请求框架
- **OkHttp 5.3.2** - HTTP 客户端
- **Kotlin Serialization 1.9.0** - JSON 序列化
- **Kotlin Coroutines 1.10.2** - 协程支持

### 基本用法

```kotlin
// 获取 API 服务实例
val animeApi = JikanApiClient.animeApi

// 调用接口（所有方法返回 Result）
lifecycleScope.launch {
    animeApi.getAnimeById(1)
        .onSuccess { response ->
            // 处理成功响应
            val anime = response.data
            println("动漫标题: ${anime?.title}")
        }
        .onFailure { exception ->
            // 处理错误
            println("请求失败: ${exception.message}")
        }
}
```

### Result 错误处理

所有 API 方法都返回 `Result<T>`，提供了丰富的错误处理扩展：

```kotlin
// 基础用法
animeApi.getAnimeById(1)
    .onSuccess { response -> /* 成功处理 */ }
    .onFailure { error -> /* 失败处理 */ }

// HTTP 错误处理
animeApi.getAnimeById(1)
    .onHttpError { httpException ->
        when (httpException.code()) {
            404 -> println("动漫不存在")
            429 -> println("请求过于频繁")
            else -> println("HTTP 错误: ${httpException.code()}")
        }
    }
    .onNetworkError { ioException ->
        println("网络连接失败")
    }

// 数据转换
val title = animeApi.getAnimeById(1)
    .mapResult { response -> response.data?.title }
    .getOrDefault("未知标题")
```

更多使用示例请参考 `examples/UsageExamples.kt`。

### 注意事项

1. **限流规则**: Jikan API 有请求频率限制，请合理使用
2. **错误处理**: 使用 Result 的扩展方法进行完善的错误处理
3. **数据缓存**: 建议对数据进行本地缓存以提升性能
4. **协程支持**: 所有接口方法都是 suspend 函数，需在协程作用域中调用
5. **可空字段**: 部分数据类字段可能为 null，请注意空安全处理

## 许可证

本模块遵循项目的开源许可证。Jikan API 本身是开源且免费的。

## 相关链接

- [Jikan API 官方文档](https://docs.api.jikan.moe/)
- [Jikan GitHub 仓库](https://github.com/jikan-me/jikan)
- [MyAnimeList 官网](https://myanimelist.net/)

