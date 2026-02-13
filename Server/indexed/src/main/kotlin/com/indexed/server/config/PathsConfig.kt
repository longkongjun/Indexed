package com.indexed.server.config

import io.ktor.server.config.ApplicationConfig

/**
 * 程序内部目录配置：提供 data、logs、cache、temp 四个路径，供数据库/日志/缓存/临时文件使用。
 *
 *
 * 不读配置文件；基准目录为 {user.home}/.indexed，其下子目录依次为 data、logs、cache、temp。
 */
data class PathsConfig(
    val data: String,
    val logs: String,
    val cache: String,
    val temp: String
) {
    companion object {
        private fun base() = "${System.getProperty("user.home")}/.indexed"

        fun from(config: ApplicationConfig): PathsConfig {
            val b = base()
            return PathsConfig(
                data = "$b/data",
                logs = "$b/logs",
                cache = "$b/cache",
                temp = "$b/temp"
            )
        }
    }
}
