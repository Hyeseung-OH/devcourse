package com.back

import com.back.domain.system.controller.SystemController
import com.back.domain.wiseSaying.controller.WiseSayingController

class App(
    val wiseSayingController: WiseSayingController = WiseSayingController(),
    val systemController: SystemController = SystemController()
) {

    fun run() {

        println("== 명언 앱 ==")

        while (true) {
            print("명언) ")
            val input = readln().trim()

            val rq = Rq(input)

            when (rq.action) {
                "종료" -> {
                    systemController.exit()
                    break
                }
                "등록" -> wiseSayingController.write()
                "목록" -> wiseSayingController.list()
                "삭제" -> wiseSayingController.delete(rq)
                "수정" -> wiseSayingController.modify(rq)
            }
        }
    }
}