package com.back.domain.wiseSaying.repository

import com.back.domain.wiseSaying.entity.WiseSaying
import com.back.global.appConfig.AppConfig
import com.back.standard.ut.JsonUtil
import java.nio.file.Path

class WiseSayingFileRepository : WiseSayingRepository {

    val tableDirPath: Path
        get() {
            return AppConfig.dbDirPath.resolve("wiseSaying")
        }


    override fun save(wiseSaying: WiseSaying): WiseSaying {
        return wiseSaying
            .takeIf { it.isNew() }
            .also {
                wiseSaying.id = genNextId()
                saveOnDisk(wiseSaying)
            } ?: wiseSaying
    }

    fun saveLastId(lastId: Int) {
        mkTableDirsIfNotExists()

        tableDirPath.resolve("lastId.txt")
            .toFile()
            .writeText(lastId.toString())
    }

    fun loadLastId(): Int {
        return kotlin.runCatching {
            tableDirPath.resolve("lastId.txt")
                .toFile()
                .readText()
                .toInt()
        }.getOrElse { 0 }
    }

    fun genNextId(): Int {
        return (loadLastId() + 1).also {
            saveLastId(it)
        }
    }

    private fun saveOnDisk(wiseSaying: WiseSaying) {
        mkTableDirsIfNotExists()

        val wiseSayingFile = tableDirPath.resolve("${wiseSaying.id}.json")
        wiseSayingFile.toFile().writeText(wiseSaying.jsonStr)
    }

    private fun mkTableDirsIfNotExists() {
        tableDirPath.toFile().run {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    override fun delete(wiseSaying: WiseSaying) {
        tableDirPath
            .resolve("${wiseSaying.id}.json")
            .toFile()
            .takeIf { it.exists() }
            ?.delete()
    }

    override fun findAll(): List<WiseSaying> {
        return tableDirPath.toFile()
            .listFiles()
            ?.filter { it.name != "data.json" }
            ?.filter { it.name.endsWith(".json") }
            ?.map { it.readText() }
            ?.map(WiseSaying.Companion::fromJsonStr)
            .orEmpty()
    }

    override fun findById(id: Int): WiseSaying? {
        return tableDirPath
            .toFile()
            .listFiles()
            ?.find { it.name == "${id}.json" }
            ?.let { WiseSaying.fromJsonStr(it.readText()) }
    }

    override fun clear() {
        tableDirPath.toFile().deleteRecursively()
    }

    override fun build() {
        mkTableDirsIfNotExists()

        val mapList = findAll()
            .map(WiseSaying::map)

        JsonUtil.toString(mapList)
            .let {
                tableDirPath
                    .resolve("data.json")
                    .toFile()
                    .writeText(it)
            }
    }

    private fun filterByKeyword(
        keyword: String,
        selector: (WiseSaying) -> String
    ): List<WiseSaying> {

        val pure = keyword.replace("%", "")
        val wiseSayings = findAll()

        if (pure.isBlank()) return wiseSayings

        return when {
            keyword.startsWith("%") && keyword.endsWith("%") ->
                wiseSayings.filter { selector(it).contains(pure) }
            keyword.startsWith("%") ->
                wiseSayings.filter { selector(it).endsWith(pure) }
            keyword.endsWith("%") ->
                wiseSayings.filter { selector(it).startsWith(pure) }
            else ->
                wiseSayings.filter { selector(it) == pure }
        }
    }

    override fun findByAuthorLike(authorLike: String): List<WiseSaying> {
        return filterByKeyword(authorLike) { it.author }
    }

    override fun findByAuthorContent(contentLike: String): List<WiseSaying> {
        return filterByKeyword(contentLike) { it.content }
    }

}