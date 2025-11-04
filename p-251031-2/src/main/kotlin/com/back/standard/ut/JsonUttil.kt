package com.back.standard.ut

object JsonUtil {

    // Map 리스트를 JSON 문자열로 변환
    fun toString(mapList: List<Map<String, Any?>>): String {
        return mapList.joinToString(
            prefix = "[\n", separator = ",\n", postfix = "\n]"
        ) { map -> toString(map).prependIndent("    ") }
    }

    // Map을 JSON 문자열로 변환
    fun toString(map: Map<String, Any?>): String {
        return map.entries.joinToString(
            prefix = "{\n", separator = ",\n", postfix = "\n}"
        ) { (key, value) ->
            val formattedKey = "\"$key\""
            val formattedValue = when (value) {
                is String -> "\"$value\""
                else -> value
            }
            "    $formattedKey: $formattedValue"
        }
    }

    fun jsonStrToMap(jsonStr: String): Map<String, Any> {
        return jsonStr
            .removeSurrounding("{", "}")
            .split(",")
            .mapNotNull {
                val keyValue = it
                    .split(":", limit = 2)
                    .map(String::trim)

                if (keyValue.size != 2) return@mapNotNull null

                val key = keyValue[0].removeSurrounding("\"")
                val value = if (keyValue[1].startsWith("\"") && keyValue[1].endsWith("\"")) {
                    keyValue[1].removeSurrounding("\"")
                } else {
                    keyValue[1].toInt()
                }

                key to value
            }.toMap()
    }
}