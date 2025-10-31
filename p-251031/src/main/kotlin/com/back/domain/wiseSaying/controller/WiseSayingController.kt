package com.back.domain.wiseSaying.controller

import com.back.Rq
import com.back.domain.wiseSaying.service.WiseSayingService

class WiseSayingController(
    val wiseSayingService: WiseSayingService = WiseSayingService()
){

    fun write() {
        print("명언: ")
        val content = readln().trim()

        print("작가: ")
        val author = readln().trim()

        val wiseSaying = wiseSayingService.write(content, author)
        println("${wiseSaying.id}번 명언이 등록되었습니다.")
    }

    fun list()  {
        println("번호 / 작가 / 명언")
        println("----------------------")

        wiseSayingService.findAll().reversed().forEach {
            println("${it.id} / ${it.author} / ${it.content}")
        }
    }

    fun delete(rq: Rq) {
        val id = rq.getParamValueAsInt("id", 0)

        if (id == 0) {
            println("id를 정확히 입력해주세요.")
            return
        }

        val wiseSaying = wiseSayingService.findById(id)

        if (wiseSaying == null) {
            println("${id}번 명언은 존재하지 않습니다.")
            return
        }

        wiseSayingService.delete(wiseSaying)
        println("${id}번 명언이 삭제되었습니다.")
    }

    fun modify(rq: Rq) {

        val id = rq.getParamValueAsInt("id", 0)

        if (id == 0) {
            println("id를 정확히 입력해주세요.")
            return
        }

        val wiseSaying = wiseSayingService.findById(id)

        if (wiseSaying == null) {
            println("${id}번 명언은 존재하지 않습니다.")
            return
        }

        println("명언(기존) : ${wiseSaying.content}): ")
        print("명언 : ")
        val newContent = readln().trim()
        println("작가(기존: ${wiseSaying.author}): ")
        print("작가 : ")
        val newAuthor = readln().trim()

        wiseSayingService.modify(wiseSaying, newContent, newAuthor)
        println("${id}번 명언이 수정되었습니다.")
    }
}