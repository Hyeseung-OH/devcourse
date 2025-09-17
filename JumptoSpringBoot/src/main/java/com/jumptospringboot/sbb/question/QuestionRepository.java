package com.jumptospringboot.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String subject);
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);

    Page<Question> findAll(Pageable pageable);
    // Specification과 Pageable 객체를 사용하여 DB에서 Question 엔티티를 조회한 결과를 페이징하여 반환
    Page<Question> findAll(Specification<Question> specification,  Pageable pageable);


    /*
    @Query는 반드시 테이블 기준이 아닌 엔티티 기준으로 작성
    @Query("select "
            + "distinct q "
            + "from Question q "
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")

    @Qurey에 매개변수로 전달할 문자열은 @Param 어노테이션 사용해야 함
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable); */
}
