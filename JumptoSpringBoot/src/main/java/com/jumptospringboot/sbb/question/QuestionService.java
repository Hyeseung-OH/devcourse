package com.jumptospringboot.sbb.question;

import com.jumptospringboot.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// 서비스가 필요한 이유
// 1. 복잡한 코드를 모듈화할 수 있음
// 2. 엔티티 객체를 DTO 객체로 변환할 수 있음
//  스프링이 서비스로 인식하게 하기 위한 어노테이션
@Service
// 생성자 자동 주입 어노테이션
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    // 질문 목록 데이터를 조회하여 리턴 => 원래 Repository에서 하던 일
    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    // 상세 페이지에 서비스 활용
    public Question getQuestion(Integer id) {
        // Optional 객체는 값이 있을 수도 있고, 없을 수도 있을 때 사용
        Optional<Question> question = this.questionRepository.findById(id);
        // 값의 존재 여부를 검증해야 함
        if(question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    // 질문 데이터 저장
    public void create(String subject, String content) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }
}
