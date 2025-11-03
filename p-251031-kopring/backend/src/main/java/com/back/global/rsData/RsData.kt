package com.back.global.rsData

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.AllArgsConstructor
import lombok.Getter

@AllArgsConstructor
@Getter
// @JvmOverloads: 이게 붙어야 자바에서 인지할 수 있음
class RsData<T> @JvmOverloads constructor(
    val resultCode: String,
    val msg: String,
    val data: T? = null
) {
//    constructor(resultCode: String, msg: String) : this(
//        resultCode,
//        msg,
//        null as T
//    )
    @get:JsonIgnore
    val statusCode: Int
        get() {
            val statusCode =
                resultCode.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            return statusCode.toInt()
        }
}
