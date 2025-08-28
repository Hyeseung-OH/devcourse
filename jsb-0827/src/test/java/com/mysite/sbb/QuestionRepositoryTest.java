package com.mysite.sbb;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

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

    @Test
    @DisplayName("데이터 삭제" )
    void t7() {
        assertEquals(2, this.questionRepository.count());
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        this.questionRepository.delete(q);
        assertEquals(1, this.questionRepository.count());
    }

    @Test
    @DisplayName("답변 데이터 생성 - repository 버전" )
    void t8() {
        Question question = this.questionRepository.findById(2).get();

        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다.");
        a.setQuestion(question);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
        a.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(a);
    }

    @Test
    @DisplayName("답변 데이터 생성 - OneToMany 버전" )
    @Transactional
    @Rollback(false)
    void t9() {
        Question question2 = this.questionRepository.findById(2).get();

        int beforeSize = question2.getAnswerList().size();
        question2.addAnswer("네 자동으로 생성됩니다.");

        int afterSize = question2.getAnswerList().size();
        assertEquals(beforeSize + 1, afterSize);
    }

    @Test
    @DisplayName("2번 질문의 답글 조회")
    void t10() {
        Question q2 = questionRepository.findById(2).get();
        Answer answer = q2.getAnswerList().get(0);

        System.out.println("answer = " + answer.getContent());
    }
}
