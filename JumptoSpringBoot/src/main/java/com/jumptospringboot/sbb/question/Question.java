package com.jumptospringboot.sbb.question;

import com.jumptospringboot.sbb.answer.Answer;
import com.jumptospringboot.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    // 질문과 답변은 1:N 관계
    // cascade = CascadeType.REMOVE: 질문을 삭제하면 답변도 함께 삭제
    // 질문 하나에 답변은 여러 개이므로 List<Answer>
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    // 사용자 한 명이 질문 여러 개 작성할 수 있음
    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

    @ManyToMany
    Set<SiteUser> voter; // voter 속성값이 서로 중복되지 않도록 하기 위함
}
