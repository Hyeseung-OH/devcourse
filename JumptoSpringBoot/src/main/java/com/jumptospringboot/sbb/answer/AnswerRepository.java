package com.jumptospringboot.sbb.answer;

import com.jumptospringboot.sbb.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    // 특정 질문에 대한 답변들을 페이징과 정렬로 조회
    Page<Answer> findByQuestion(Question question, Pageable pageable);
    
    // 추천수 기준으로 정렬된 답변 조회 (JPQL 사용)
    @Query("SELECT a FROM Answer a " +
           "WHERE a.question = :question " +
           "ORDER BY SIZE(a.voter) DESC, a.createDate DESC")
    Page<Answer> findByQuestionOrderByVoteCountDesc(@Param("question") Question question, Pageable pageable);
}
