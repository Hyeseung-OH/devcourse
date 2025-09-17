package com.jumptospringboot.sbb.answer;

import com.jumptospringboot.sbb.question.Question;
import com.jumptospringboot.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
// 원래 entity에는 setter 안 씀
@Setter
@Entity
public class Answer {
    @Id // id 속성을 기본키로 지정
    // @GeneratedValue: 값을 일일이 입력하지 않아도 자동으로 1씩 증가
    // strategy = GenerationType.IDENTITY: 고유한 번호를 생성하는 방법을 지정하는 부분
    // GenerationType.IDENTITY: 해당 속성만 별도로 번호가 차례대로 늘어나도록
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Entity는 Column을 사용하지 않더라도 테이블의 열로 인식하긴 함
    // columnDefinition: 열 데이터의 유형이나 성격을 정의
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    // 질문 엔티티와 연결된 속성이라는 것을 표시
    // ManyToOne: N:1 관계
    // Answer(자식) 엔티티의 question 속성과 Question(부모) 엔티티가 서로 연결됨 => 외래키 생성
    @ManyToOne
    private Question question;

    // 사용자 한 명이 댓글 여러 개 작성할 수 있음
    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;
}
