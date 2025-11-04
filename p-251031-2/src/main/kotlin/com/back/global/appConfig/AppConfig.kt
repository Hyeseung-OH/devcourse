package com.back.global.appConfig

import java.nio.file.Path

object AppConfig {
    private var mode = "dev"

    fun setModeToTest() {
        mode = "test"
    }

    fun setModeToDev() {
        mode = "dev"
    }

    val dbDirPath: Path
        get() {
            return Path.of("data/db/${mode}")
        }
}