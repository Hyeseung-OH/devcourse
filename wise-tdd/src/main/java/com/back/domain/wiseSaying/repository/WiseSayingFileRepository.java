package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.standard.util.Util;

import java.util.Map;

public class WiseSayingFileRepository {
    public void save(WiseSaying wiseSaying) {
        if(wiseSaying.isNew()) {
            wiseSaying.setId(1);
            String jsonStr = Util.json.toString(wiseSaying.toMap());
            Util.file.set("db/wiseSaying/1.json", jsonStr);
        }
    }

    public WiseSaying findByIdOrNull(int id) {
        String jsonStr = Util.file.get("db/wiseSaying/1.json", "");

        if(jsonStr.isEmpty()) {
            return null;
        }

        Map<String, Object> map = Util.json.toMap(jsonStr);
        WiseSaying wiseSaying = new WiseSaying(map);

        return wiseSaying;
    }
}
