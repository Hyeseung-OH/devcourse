package com.mysite.sbb;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (length = 200)
    private String subject;

    @Column (columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    // 선택
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Answer> answerList = new ArrayList<>();

    public void addAnswer(String s) {
        Answer answer = new Answer();
        answer.setContent(s);
        answer.setQuestion(this);
        answerList.add(answer);
    }
}
