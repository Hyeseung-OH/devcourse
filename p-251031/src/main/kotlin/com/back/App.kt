package com.back

import com.back.domain.system.controller.SystemController
import com.back.domain.wiseSaying.controller.WiseSayingController
import com.back.domain.wiseSaying.entity.WiseSaying

class App(
    val wiseSayingController: WiseSayingController = WiseSayingController(),
    val systemController: SystemController = SystemController()
) {
    fun run() {
        var lastId = 0
        val wiseSayings = mutableListOf<WiseSaying>()

        println("== 명언 앱 ==")

        while (true) {
            print("명언) ")
            val input = readln().trim()

            val rq: Rq = Rq(input)

            when (rq.action) {
                "종료" -> systemController.exit()
                "등록" -> wiseSayingController.write()
                "목록" -> wiseSayingController.list()
                "수정" -> wiseSayingController.modify(rq)
                "삭제" -> wiseSayingController.delete(rq)
            }
        }
    }
}