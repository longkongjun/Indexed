package com.indexed.server.config

import io.ktor.server.config.ApplicationConfig

/**
 * 整理输出目录配置：提供整理后展示根路径，供写入链接/复制与刮削元数据。
 *
 *
 * 从 application.yaml 的 indexed.scraping.organized 读取；程序在此目录下按整理结构写入。
 */
data class ScrapingOutputConfig(val path: String) {
    companion object {
        fun from(config: ApplicationConfig) =
            ScrapingOutputConfig(path = config.property("indexed.scraping.organized").getString())
    }
}
