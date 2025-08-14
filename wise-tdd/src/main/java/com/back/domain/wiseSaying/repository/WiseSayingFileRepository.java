package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.standard.util.Util;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WiseSayingFileRepository {
    private static String dbPath = "db/wiseSaying";

    public static void clear() {
        Util.file.delete(dbPath);
    }

    private String getFilePath(int id) {
        return dbPath + "/%d.json".formatted(id);
    }

    private String getLastIdPath() {
        return dbPath + "/lastId.txt";
    }

    public WiseSaying save(WiseSaying wiseSaying) {

        if(wiseSaying.isNew()) {
            incrementLastId();
            int lastId = getLastId();
            wiseSaying.setId(lastId);
        }

        String jsonStr = Util.json.toString(wiseSaying.toMap());
        Util.file.set(getFilePath(wiseSaying.getId()), jsonStr);
        return wiseSaying;
    }

    private void incrementLastId() {
        Util.file.set(getLastIdPath(), String.valueOf(getLastId() + 1));
    }

    private int getLastId() {

        return Util.file.getAsInt(getLastIdPath(), 0);
    }

    public Optional<WiseSaying> findById(int id) {
        String jsonStr = Util.file.get(getFilePath(id), "");

        if(jsonStr.isEmpty()) {
            return Optional.empty();
        }

        Map<String, Object> map = Util.json.toMap(jsonStr);
        WiseSaying wiseSaying = new WiseSaying(map);

        return Optional.of(wiseSaying);
    }

    public boolean delete(WiseSaying wiseSaying){
        return Util.file.delete(getFilePath(wiseSaying.getId()));
    }

    public List<WiseSaying> findAll() {
        return Util.file.walkRegularFiles(dbPath, "\\d+\\.json$")
                .map(path -> Util.file.get(path.toString(), ""))
                .map(Util.json::toMap)
                .map(WiseSaying::new)
                .toList();
    }
}
