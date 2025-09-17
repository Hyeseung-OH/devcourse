package com.jumptospringboot.sbb.question;

import com.jumptospringboot.sbb.DataNotFoundException;
import com.jumptospringboot.sbb.answer.Answer;
import com.jumptospringboot.sbb.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
//    public List<Question> getList() {
//        return this.questionRepository.findAll();
//    }

    // 페이징
    public Page<Question> getList(int page, String kw) {
        // 최신순(역순)으로 데이터 조회
        // sort.add 메서드를 통해 정렬 조건 추가 가능
        // desc 내림차순, asc 오름차순
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); // PageRequest.of(page, 10) => page는 조회할 페이지의 번호, 10은 한 페이지에 보여 줄 게시물 개수
        Specification<Question> spec = search(kw); // 검색어를 의미하는 매개변수 kw
        return this.questionRepository.findAll(spec, pageable);
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
    public void create(String subject, String content, SiteUser user) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setCreateDate(LocalDateTime.now());
        question.setAuthor(user);
        this.questionRepository.save(question);
    }

    // 질문 서비스 수정
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    // 질문 삭제 가능
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    // 추천 기능
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }

    // 검색 기능 - 제목, 내용, 질문작성자, 답변내용, 답변작성자를 OR 조건으로 검색
    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> questionRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); // 중복 결과 제거 (JOIN으로 인한 중복 방지)

                // u1: Question 엔티티와 SiteUser(질문 작성자) 엔티티를 LEFT JOIN
                // Question의 author 속성을 통해 연결
                Join<Question, SiteUser> u1 = questionRoot.join("author", JoinType.LEFT);

                // answerJoin: Question 엔티티와 Answer 엔티티를 LEFT JOIN
                // Question의 answerList 속성을 통해 연결 (1:N 관계)
                Join<Question, Answer> answerJoin = questionRoot.join("answerList", JoinType.LEFT);

                // u2: Answer 엔티티와 SiteUser(답변 작성자) 엔티티를 LEFT JOIN
                // Answer의 author 속성을 통해 연결
                Join<Answer, SiteUser> u2 = answerJoin.join("author", JoinType.LEFT);

                // OR 조건으로 여러 필드에서 키워드 검색 (하나라도 일치하면 결과에 포함)
                return cb.or(
                        cb.like(questionRoot.get("subject"), "%" + kw + "%"),    // 질문 제목에서 검색
                        cb.like(questionRoot.get("content"), "%" + kw + "%"),    // 질문 내용에서 검색
                        cb.like(u1.get("username"), "%" + kw + "%"),            // 질문 작성자명에서 검색
                        cb.like(answerJoin.get("content"), "%" + kw + "%"),      // 답변 내용에서 검색
                        cb.like(u2.get("username"), "%" + kw + "%")             // 답변 작성자명에서 검색
                );
            }
        };
    }
}
