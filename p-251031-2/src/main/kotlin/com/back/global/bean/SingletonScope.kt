package com.back.global.bean

import com.back.domain.system.controller.SystemController
import com.back.domain.wiseSaying.controller.WiseSayingController
import com.back.domain.wiseSaying.repository.WiseSayingFileRepository
import com.back.domain.wiseSaying.repository.WiseSayingMemRepository
import com.back.domain.wiseSaying.service.WiseSayingService

object SingletonScope {

    val wiseSayingMemRepository by lazy { WiseSayingMemRepository() }
    val wiseSayingFileRepository by lazy { WiseSayingFileRepository() }
    val wiseSayingService by lazy { WiseSayingService(wiseSayingFileRepository) }
    val wiseSayingController by lazy { WiseSayingController(wiseSayingService) }
    val systemController by lazy { SystemController() }


}