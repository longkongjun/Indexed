package com.indexed.server.storage

import java.io.File

/**
 * 列出资源来源目录下直接子项的结果。
 */
sealed class ListSourceResult {
    data class Ok(val root: String, val entries: List<Entry>) : ListSourceResult()
    data class NotFound(val path: String) : ListSourceResult()
    data class NotDirectory(val path: String) : ListSourceResult()

    data class Entry(val name: String, val isDirectory: Boolean)
}

/**
 * 列出指定路径（资源来源根）下的直接子项。
 * 路径必须在来源根约束内由调用方保证；此处仅做存在性与类型校验。
 */
fun listSourceRoot(rootPath: String): ListSourceResult {
    val root = File(rootPath)
    if (!root.exists()) return ListSourceResult.NotFound(rootPath)
    if (!root.isDirectory) return ListSourceResult.NotDirectory(rootPath)
    val entries = root.listFiles()?.map { f ->
        ListSourceResult.Entry(name = f.name, isDirectory = f.isDirectory)
    } ?: emptyList()
    return ListSourceResult.Ok(rootPath, entries)
}
