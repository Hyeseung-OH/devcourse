package com.mysite.sbb;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

	@Test
    @DisplayName("findAll()")
    void t1() {
        List<Question> all = this.questionRepository.findAll();
        assertEquals(2, all.size());

        Question q = all.get(0);
        assertEquals("sbb가 무엇인가요?", all.get(0).getSubject());
	}

    @Test
    @DisplayName("findById()")
    void t2() {
        Optional<Question> oq = this.questionRepository.findById(1);

        if(oq.isPresent()) {
            Question q = oq.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }

    @Test
    @DisplayName("findBySubject()")
    void t3() {
        Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?").get();
        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("findBySubjectAndContent()")
    void t4() {
        Question q = this.questionRepository.findBySubjectAndContent(
                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.").get();
        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("findBySubjectLike" )
    void t5() {
        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    @DisplayName("데이터 수정")
    void t6() {
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        q.setSubject("수정된 제목");

        this.questionRepository.save(q);

        // 변경된 제목으로 다시 조회, 검증 확인
        Question q2 = this.questionRepository.findById(1).get();
        assertEquals("수정된 제목", q2.getSubject());
    }
}
