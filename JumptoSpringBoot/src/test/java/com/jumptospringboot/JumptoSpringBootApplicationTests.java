package com.jumptospringboot;

import com.jumptospringboot.sbb.answer.Answer;
import com.jumptospringboot.sbb.answer.AnswerRepository;
import com.jumptospringboot.sbb.question.Question;
import com.jumptospringboot.sbb.question.QuestionRepository;
import com.jumptospringboot.sbb.question.QuestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// 테스트 클래스임을 명시
@SpringBootTest
class JumptoSpringBootApplicationTests {
    // Autowired: 의존성 주입 - 스프링이 객체를 대신 생성하여 주입
    // 객체를 주입하는 방식에는 1. @Autowired, 2. @Setter, 3. 생성자 사용
    // 그러나 순환 참조 문제가 발생할 수 있으므로 테스트 환경 외에선 @Autowired 자제
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerRepository answerRepository;

    // 테스트 메서드임을 명시
    @Test
    @DisplayName("글 생성")
    void test1() {
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 두번째 질문 저장
    }

    @Test
    @DisplayName("findAll: 질문 데이터 조회")
    void test2() {
        List<Question> all = this.questionRepository.findAll();
        // assertEquals(기댓값, 실젯값)
        assertEquals(2, all.size());

        Question q = all.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    @DisplayName("findById: 질문 데이터 조회")
    void test3() {
        // findById의 리턴 타입은 Optional => 값이 존재할 수도, 없을 수도 있기 때문
        Optional<Question> oq = this.questionRepository.findById(1);
        if(oq.isPresent()) {
            Question q = oq.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }

    @Test
    @DisplayName("findBySubject: 질문 데이터 조회")
    void test4() {
        // findBySubject는 기본적으로 제공되지 않는 메서드 => QuestionRepository 인터페이스에 추가
        // 이게 가능한 이유: JPA에는 리포지터리의 메서드명을 분석해 쿼리를 만들고 실행하는 기능이 있기 때문
        // findBy + 엔티티 속성명 => 입력한 속성의 값으로 데이터를 조회할 수 있음
        // 콘솔에서 findBySubject가 실행될 때 sql 쿼리문이 where subject로 변환되어 있는 것 확인 가능
        Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("findBySubjectAndContent: 질문 데이터 조회")
    void test5() {
        // where문에는 And, Or, Between, LessThan, GreaterThanEqual, Like, In, OrderBy 연산자 사용 가능
        // 응답 결과가 여러 건인 경우에는 무조건 리턴 타입이 List<Question>
        // And 연산자를 활용하면 여러 조건을 결합해 데이터 조회 가능
        Question q = this.questionRepository.findBySubjectAndContent(
                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("findBySubjectLike: 질문 데이터 조회")
    void test6() {
        // 특정 문자열을 포함하는 데이터 조회
        // sbb%, %sbb, %sbb%와 같이 %의 위치에 따라 의미가 달라짐
        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    @DisplayName("질문 데이터 수정")
    void test7() {
        Optional<Question> oq = this.questionRepository.findById(1);
        // assertTrue(): 괄호의 값이 참인지 테스트
        assertTrue(oq.isPresent());
        Question q = oq.get();
        q.setSubject("수정된 제목");
        this.questionRepository.save(q);
    }

    @Test
    @DisplayName("질문 데이터 삭제")
    void test8() {
        // count 메서드는 테이블 행의 개수를 리턴함
        assertEquals(2, this.questionRepository.count());
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        // 리포지토리의 delete 메서드를 사용해 데이터 삭제
        this.questionRepository.delete(q);
        assertEquals(1, this.questionRepository.count());
    }


    @Test
    @DisplayName("답변 데이터 저장")
    void test9() {
        // id가 2인 질문 데이터를 가져와 답변의 question 속성에 대입
        Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question question = oq.get();

        Answer answer = new Answer();
        answer.setContent("네 자동으로 생성됩니다.");
        answer.setQuestion(question); // 어떤 질문의 답변인지 알기 위해선 Question 객체가 필요함
        answer.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    @Test
    @DisplayName("findById: 답변 데이터 조회")
    void test10() {
        Optional<Answer> oa = this.answerRepository.findById(1);
        assertTrue(oa.isPresent());
        Answer a = oa.get();
        assertEquals(2, a.getQuestion().getId());
    }

    @Test
    @DisplayName("질문 데이터를 통해 답변 데이터 찾기")
    // 실패함,
    void test11() {
        Optional<Question> oq = this.questionRepository.findById(2);
        // DB 세션(= 스트링 부트 애플리케이션과 데이터베이스 간의 연결) 끊김
        assertTrue(oq.isPresent());
        Question question = oq.get();

        // 세션이 종료됐기 때문에 정상적으로 동작하지 않음
        // question.getAnswerList()가 동작할 수 없음
        // 데이터를 필요한 시점에 가져오는 방식을 지연(Lazy) 방식이라고 하며,
        // 반대로 미리 모든 데이터를 가져오는 방식을 즉시(Eager) 방식이라고 함
        // 근데 이런 오류는 테스트 환경에서만 발생함
        List<Answer> answerList = question.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
    }

    @Test
    @DisplayName("질문 데이터를 통해 답변 데이터 찾기")
    @Transactional // 트랜잭션을 사용하면 세션이 종료되어도 데이터를 가져올 수 있음
    void test12() {
        Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question question = oq.get();

        List<Answer> answerList = question.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
    }

    @Test
    @DisplayName("300개의 테스트 데이터를 생성하는 테스트 케이스")
    void test13() {
        for (int i = 1; i <= 300; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            this.questionService.create(subject, content, null);
        }
    }
}
