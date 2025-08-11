package repository;

import org.example.WiseSaying;

import java.util.ArrayList;
import java.util.List;

public class WiseSayingRepository {
    private List<WiseSaying> wiseSayings = new ArrayList<>();
    private int lastId = 0;

    public WiseSaying findByIdOrNull(int id) {
        return wiseSayings.stream()
                .filter(w -> w.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean delete(int id) {
        return wiseSayings.removeIf(w -> w.getId() == id);
    }

    public WiseSaying save(WiseSaying wiseSaying) {
        if(wiseSaying.isNew()){ // wiseSaying이 새로운 객체인 경우
            lastId++;
            wiseSaying.setId(lastId);
            wiseSayings.add(wiseSaying);
        }
        return wiseSaying;
    }

    public List<WiseSaying> findListDesc() {
        return wiseSayings.reversed();
    }
}