package com.back

import com.back.domain.system.controller.SystemController
import com.back.domain.wiseSaying.controller.WiseSayingController
import com.back.global.bean.SingletonScope
import com.back.global.rq.Rq

class App(
    val wiseSayingController: WiseSayingController = SingletonScope.wiseSayingController,
    val systemController: SystemController = SingletonScope.systemController
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
                "목록" -> wiseSayingController.list(rq)
                "삭제" -> wiseSayingController.delete(rq)
                "수정" -> wiseSayingController.modify(rq)
                "빌드" -> wiseSayingController.build()
            }
        }
    }
}