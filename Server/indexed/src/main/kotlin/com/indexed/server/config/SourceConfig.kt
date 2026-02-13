package com.indexed.server.config

import io.ktor.server.config.ApplicationConfig

/**
 * 资源来源目录配置：提供原始资源文件所在路径，供存储/扫描只读使用。
 *
 *
 * 从 application.yaml 的 indexed.source.root 读取；程序不写该目录。
 */
data class SourceConfig(val root: String) {
    companion object {
        fun from(config: ApplicationConfig) =
            SourceConfig(root = config.property("indexed.source.root").getString())
    }
}
