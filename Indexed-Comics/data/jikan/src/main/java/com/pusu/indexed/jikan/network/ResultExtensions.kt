package com.pusu.indexed.jikan.network

import java.io.IOException

/**
 * Result 扩展函数
 *
 * 为 Kotlin Result 类型提供额外的便捷方法，专注于网络请求场景。
 * 注意：Kotlin 标准库已提供 onSuccess、onFailure、map、getOrDefault 等方法，请直接使用。
 */

/**
 * 在 HTTP 错误时执行操作
 *
 * 用于处理 Retrofit 返回的 HTTP 错误（如 404, 500 等）
 *
 * @param action 处理 HTTP 错误的操作，接收 HttpException
 * @return 原始 Result，支持链式调用
 *
 * @sample
 * ```kotlin
 * result.onHttpError { exception ->
 *     when (exception.code()) {
 *         404 -> println("资源不存在")
 *         500 -> println("服务器错误")
 *     }
 * }
 * ```
 */
inline fun <T> Result<T>.onHttpError(action: (HttpException) -> Unit): Result<T> {
    exceptionOrNull()?.let { exception ->
        if (exception is HttpException) {
            action(exception)
        }
    }
    return this
}

/**
 * 在网络错误时执行操作
 *
 * 用于处理网络连接失败、超时等 I/O 错误（不包括 HTTP 错误）
 *
 * @param action 处理网络错误的操作，接收 IOException
 * @return 原始 Result，支持链式调用
 *
 * @sample
 * ```kotlin
 * result.onNetworkError { exception ->
 *     println("网络连接失败: ${exception.message}")
 * }
 * ```
 */
inline fun <T> Result<T>.onNetworkError(action: (IOException) -> Unit): Result<T> {
    exceptionOrNull()?.let { exception ->
        // 只处理 IOException 且不是 HttpException（HttpException 继承自 IOException）
        when (exception) {
            is HttpException -> {} // 忽略 HTTP 错误
            is IOException -> action(exception) // 处理网络错误
        }
    }
    return this
}

/**
 * 读取服务端返回的结构化错误响应（若存在且可解析）
 */
fun HttpException.errorResponseOrNull() = errorResponse

/**
 * 扁平化嵌套的 Result
 *
 * 当 transform 函数返回另一个 Result 时使用，避免 Result<Result<T>> 嵌套
 * 类似于 Kotlin 集合的 flatMap 操作
 *
 * @param transform 转换函数，接收当前值并返回新的 Result
 * @return 扁平化后的 Result
 *
 * @sample
 * ```kotlin
 * val result: Result<User> = getUserResult()
 *     .flatMap { user ->
 *         getProfileResult(user.id) // 返回 Result<Profile>
 *     }
 * ```
 */
inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return fold(transform) {
        Result.failure(it)
    }
}

// ============================================================================
// 合并多个 Result 的扩展方法
// ============================================================================

/**
 * 合并两个 Result
 *
 * 只有当两个 Result 都成功时，才返回成功的结果
 * 如果任一 Result 失败，返回第一个失败的 Result
 *
 * @param other 另一个 Result
 * @param transform 合并函数，接收两个成功值并返回合并后的值
 * @return 合并后的 Result
 *
 * @sample
 * ```kotlin
 * val result1: Result<User> = getUserResult()
 * val result2: Result<Profile> = getProfileResult()
 *
 * val combined = result1.zip(result2) { user, profile ->
 *     UserWithProfile(user, profile)
 * }
 * ```
 */
inline fun <T1, T2, R> Result<T1>.zip(
    other: Result<T2>,
    transform: (T1, T2) -> R,
): Result<R> {
    return flatMap { t ->
        other.map { r ->
            transform(t, r)
        }
    }
}

/**
 * 合并三个 Result
 *
 * 只有当三个 Result 都成功时，才返回成功的结果
 *
 * @sample
 * ```kotlin
 * val result1 = getAnimeResult()
 * val result2 = getMangaResult()
 * val result3 = getCharacterResult()
 *
 * val combined = result1.zip(result2, result3) { anime, manga, character ->
 *     CombinedData(anime, manga, character)
 * }
 * ```
 */
inline fun <T1, T2, T3, R> Result<T1>.zip(
    result2: Result<T2>,
    result3: Result<T3>,
    transform: (T1, T2, T3) -> R,
): Result<R> {
    return flatMap { t1 ->
        result2.flatMap { t2 ->
            result3.map { t3 ->
                transform(t1, t2, t3)
            }
        }
    }
}

/**
 * 合并可迭代集合中的所有 Result
 *
 * 只有当所有 Result 都成功时，才返回成功的结果列表
 * 如果任一 Result 失败，返回第一个失败的 Result
 *
 * 此方法支持 List、Set、Sequence 等所有可迭代类型
 * 对于 Collection 类型会自动优化性能（预分配容量）
 *
 * @return 包含所有成功值的 Result，或第一个失败的 Result
 *
 * @sample
 * ```kotlin
 * // List
 * val listResults = listOf(
 *     getAnimeResult(1),
 *     getAnimeResult(2),
 *     getAnimeResult(3)
 * )
 * listResults.combine()
 *
 * // Set
 * val setResults: Set<Result<Anime>> = ...
 * setResults.combine()
 *
 * // Sequence
 * val seqResults = ids.asSequence().map { getAnimeResult(it) }
 * seqResults.combine()
 * ```
 */
fun <T> Iterable<Result<T>>.combine(): Result<List<T>> {
    return runCatching {
        when (this@combine) {
            is Collection -> buildList(size) {
                for (result in this@combine) {
                    add(result.getOrThrow())
                }
            }
            else -> buildList {
                for (result in this@combine) {
                    add(result.getOrThrow())
                }
            }
        }
    }
}
