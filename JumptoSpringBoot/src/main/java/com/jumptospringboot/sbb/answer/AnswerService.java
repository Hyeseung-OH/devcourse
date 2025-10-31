package com.jumptospringboot.sbb.answer;

import com.jumptospringboot.sbb.DataNotFoundException;
import com.jumptospringboot.sbb.question.Question;
import com.jumptospringboot.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    // 답변 데이터 생성
    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);

        return answer;
    }

    // 답변 데이터 조회
    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if(answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    // 답변 데이터 수정
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    // 답변 데이터 삭제
    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    // 답변 데이터 추천
    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }

    // 특정 질문의 답변들을 페이징과 정렬로 조회
    public Page<Answer> getAnswerList(Question question, int page, String sort) {
        Pageable pageable = PageRequest.of(page, 5); // 페이지당 5개씩
        
        if ("recommend".equals(sort)) {
            // 추천순 정렬: 추천수 내림차순, 같으면 최신순
            return this.answerRepository.findByQuestionOrderByVoteCountDesc(question, pageable);
        } else {
            // 기본값은 최신순 정렬: 생성일 내림차순
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("createDate"));
            pageable = PageRequest.of(page, 5, Sort.by(sorts));
            return this.answerRepository.findByQuestion(question, pageable);
        }
    }
}
