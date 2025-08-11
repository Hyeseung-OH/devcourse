package service;

import org.example.WiseSaying;
import repository.WiseSayingRepository;

import java.util.List;

public class WiseSayingService {
    private WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();

    public void modify(WiseSaying wiseSaying, String newSaying, String newAuthor) {
        wiseSaying.setSaying(newSaying);
        wiseSaying.setAuthor(newAuthor);

        wiseSayingRepository.save(wiseSaying);
    }

    public WiseSaying write(String saying, String author) {
        WiseSaying wiseSaying = new WiseSaying(0, saying, author);
        wiseSaying = wiseSayingRepository.save(wiseSaying);

        return wiseSaying;
    }


    public List<WiseSaying> findListDesc() {
        return wiseSayingRepository.findListDesc();
    }

    public WiseSaying getByIdOrNull(int id) {
        return wiseSayingRepository.findByIdOrNull(id);
    }

    public boolean delete(int id) {
        return wiseSayingRepository.delete(id);
    }
}