package com.back.domain.wiseSaying.repository;

import com.back.AppContext;
import com.back.PageDto;
import com.back.domain.wiseSaying.entity.WiseSaying;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseSayingFileRepositoryTest {
    private WiseSayingFileRepository wiseSayingFileRepository;

    public WiseSayingFileRepositoryTest() {
        AppContext.init();
        wiseSayingFileRepository = AppContext.wiseSayingFileRepository;
    }

    @BeforeEach
    void beforeEach() {
        WiseSayingFileRepository.clear();
    }

    @AfterEach
    void afterEach() {
        WiseSayingFileRepository.clear();
    }

    @Test
    @DisplayName("명언 저장")
    void t1() {
        WiseSaying wiseSaying = new WiseSaying("꿈을 지녀라. 그러면 어려운 현실을 이길 수 있다.", "괴테");

        wiseSayingFileRepository.save(wiseSaying);

        WiseSaying foundedWiseSaying = wiseSayingFileRepository.findById(1).get();
        assertThat(foundedWiseSaying).isEqualTo(wiseSaying);

    }

    @Test
    @DisplayName("2개 이상의 명언 저장")
    void t2() {
        WiseSaying wiseSaying1 = new WiseSaying("꿈을 지녀라. 그러면 어려운 현실을 이길 수 있다.", "괴테");
        WiseSaying wiseSaying2 = new WiseSaying("너 자신을 알라", "소크라테스");

        wiseSayingFileRepository.save(wiseSaying1);
        wiseSayingFileRepository.save(wiseSaying2);

        WiseSaying foundedWiseSaying1 = wiseSayingFileRepository.findById(1).get();
        assertThat(foundedWiseSaying1).isEqualTo(wiseSaying1);

        WiseSaying foundedWiseSaying2 = wiseSayingFileRepository.findById(2).get();
        assertThat(foundedWiseSaying2).isEqualTo(wiseSaying2);
    }

    @Test
    @DisplayName("명언 삭제")
    void t3() {
        WiseSaying wiseSaying1 = new WiseSaying("꿈을 지녀라. 그러면 어려운 현실을 이길 수 있다.", "괴테");

        wiseSayingFileRepository.save(wiseSaying1);

        wiseSayingFileRepository.delete(wiseSaying1);
        WiseSaying foundedWiseSaying1 = wiseSayingFileRepository.findById(1).orElse(null);
        assertThat(foundedWiseSaying1).isNull();
    }

    @Test
    @DisplayName("명언 수정")
    void t4() {
        WiseSaying wiseSaying1 = new WiseSaying("꿈을 지녀라. 그러면 어려운 현실을 이길 수 있다.", "괴테");
        wiseSayingFileRepository.save(wiseSaying1);

        WiseSaying wiseSaying = wiseSayingFileRepository.findById(1).get();
        wiseSaying.setSaying("너 자신을 알라");
        wiseSaying.setAuthor("소크라테스");

        wiseSayingFileRepository.save(wiseSaying);

        WiseSaying foundWisewiseSaying1 = wiseSayingFileRepository.findById(1).get();
        assertThat(foundWisewiseSaying1.getId())
                .isEqualTo(1);
        assertThat(foundWisewiseSaying1.getSaying())
                .isEqualTo("너 자신을 알라");
        assertThat(foundWisewiseSaying1.getAuthor())
                .isEqualTo("소크라테스");
    }

    @Test
    @DisplayName("명언 다건 조회 - 전체 조회")
    void t5() {
        WiseSaying wiseSaying1 = new WiseSaying("꿈을 지녀라. 그러면 어려운 현실을 이길 수 있다.", "괴테");
        wiseSayingFileRepository.save(wiseSaying1);

        WiseSaying wiseSaying2 = new WiseSaying("너 자신을 알라", "괴테");
        wiseSayingFileRepository.save(wiseSaying2);

        WiseSaying wiseSaying3 = new WiseSaying("나의 죽음을 적에게 알리지 마라.", "이순신");
        wiseSayingFileRepository.save(wiseSaying3);

        List<WiseSaying> wiseSayings = wiseSayingFileRepository.findAll();

        assertThat(wiseSayings)
                .containsExactly(
                        wiseSaying1,
                        wiseSaying2,
                        wiseSaying3
                );
    }

    @Test
    @DisplayName("명언 다건 조회 - content 필터링")
    void t6() {
        WiseSaying wiseSaying1 = new WiseSaying("꿈을 지녀라. 그러면 어려운 현실을 이길 수 있다.", "괴테");
        wiseSayingFileRepository.save(wiseSaying1);

        WiseSaying wiseSaying2 = new WiseSaying("너 자신을 알라.", "괴테");
        wiseSayingFileRepository.save(wiseSaying2);

        WiseSaying wiseSaying3 = new WiseSaying("꿈은 현실이 된다.", "작자미상");
        wiseSayingFileRepository.save(wiseSaying3);

        PageDto pageDto = wiseSayingFileRepository.findByContentContainingDesc("꿈", 5, 1);

        assertThat(pageDto.getContent())
                .containsExactly(
                        wiseSaying3,
                        wiseSaying1
                );
    }
}
