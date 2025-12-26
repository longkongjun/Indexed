# Jikan API 模块

Jikan API 的 Kotlin Multiplatform 客户端实现，提供了完整的 MyAnimeList 数据访问接口。

## 📁 项目结构

```
jikan/
├── API.md                          # 完整的 API 接口文档
├── README.md                       # 本文件
├── build.gradle.kts               # 构建配置
├── src/
│   └── commonMain/
│       └── kotlin/
│           └── com/pusu/indexed/jikan/
│               ├── JikanApi.kt                    # 主 API 聚合接口
│               ├── JikanClient.kt                 # HTTP 客户端
│               ├── api/                           # API 接口定义
│               │   ├── AnimeApi.kt               # 动漫接口
│               │   ├── MangaApi.kt               # 漫画接口
│               │   ├── CharacterApi.kt           # 角色接口
│               │   ├── PeopleApi.kt              # 人物接口
│               │   ├── SeasonApi.kt              # 季度接口
│               │   ├── ProducerApi.kt            # 制作公司接口
│               │   ├── MagazineApi.kt            # 杂志接口
│               │   ├── ClubApi.kt                # 俱乐部接口
│               │   ├── UserApi.kt                # 用户接口
│               │   ├── WatchApi.kt               # 观看推荐接口
│               │   ├── GenresApi.kt              # 类型接口
│               │   ├── RandomApi.kt              # 随机接口
│               │   ├── RecommendationsApi.kt     # 推荐接口
│               │   ├── ReviewsApi.kt             # 评论接口
│               │   ├── SchedulesApi.kt           # 时间表接口
│               │   └── TopApi.kt                 # 排行榜接口
│               ├── impl/                          # API 接口实现
│               │   ├── AnimeApiImpl.kt
│               │   ├── MangaApiImpl.kt
│               │   ├── CharacterApiImpl.kt
│               │   ├── PeopleApiImpl.kt
│               │   ├── SeasonApiImpl.kt
│               │   ├── ProducerApiImpl.kt
│               │   ├── MagazineApiImpl.kt
│               │   ├── ClubApiImpl.kt
│               │   ├── UserApiImpl.kt
│               │   ├── WatchApiImpl.kt
│               │   ├── GenresApiImpl.kt
│               │   ├── RandomApiImpl.kt
│               │   ├── RecommendationsApiImpl.kt
│               │   ├── ReviewsApiImpl.kt
│               │   ├── SchedulesApiImpl.kt
│               │   └── TopApiImpl.kt
│               └── models/                       # 数据模型
│                   ├── anime/                    # 动漫相关模型
│                   ├── manga/                    # 漫画相关模型
│                   ├── character/                # 角色相关模型
│                   ├── people/                   # 人物相关模型
│                   ├── season/                   # 季度相关模型
│                   ├── producer/                 # 制作公司相关模型
│                   ├── magazine/                 # 杂志相关模型
│                   ├── club/                     # 俱乐部相关模型
│                   ├── user/                     # 用户相关模型
│                   ├── watch/                    # 观看推荐相关模型
│                   ├── genre/                    # 类型相关模型
│                   ├── random/                   # 随机相关模型
│                   ├── recommendation/           # 推荐相关模型
│                   ├── review/                   # 评论相关模型
│                   ├── schedule/                 # 时间表相关模型
│                   └── common/                   # 通用模型
│                       ├── JikanResponse.kt
│                       └── JikanPageResponse.kt
```

## 🚀 基本使用

```kotlin
import com.pusu.indexed.jikan.createJikanApi

// 创建 API 实例
val jikanApi = createJikanApi()

// 使用各个子 API
suspend fun example() {
    // 获取动漫信息
    jikanApi.anime.getAnimeById(1).fold(
        onSuccess = { response -> println(response.data.title) },
        onFailure = { error -> println("Error: ${error.message}") }
    )
    
    // 搜索漫画
    jikanApi.manga.searchManga(query = "One Piece")
    
    // 获取角色信息
    jikanApi.characters.getCharacterById(40)
    
    // 获取当前季度动漫
    jikanApi.seasons.getCurrentSeasonAnime()
    
    // 获取随机动漫
    jikanApi.random.getRandomAnime()
    
    // 获取排行榜
    jikanApi.top.getTopAnime(page = 1, limit = 10)
}
```

## 📖 完整文档

详细的 API 文档请查看 [API.md](./API.md)

## 🔗 相关链接

- [Jikan 官方文档](https://docs.api.jikan.moe/)
- [MyAnimeList](https://myanimelist.net/)
