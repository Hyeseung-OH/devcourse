package com.back.global.initData;


import com.back.domain.post.member.entity.Member;
import com.back.domain.post.member.service.MemberService;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    @Autowired
    @Lazy
    private BaseInitData self;
    private final PostService postService;
    private final MemberService memberService;

    @Bean
    ApplicationRunner initDataRunner() {
        return args -> {
            self.work1();
        };
    }

    // 생성
    @Transactional
    void work1() {
        if(postService.getTotalCount() > 0) {
            return;
        }

        Member member1 = memberService.join("systemUser", "시스템");
        Member member2 = memberService.join("adminUser", "관리자");
        Member member3 = memberService.join("user1", "유저1");
        Member member4 = memberService.join("user2", "유저2");
        Member member5 = memberService.join("user3", "유저3");

        // 서비스를 도입해서 비즈니스 로직을 재사용
        postService.write(member3, "제목1", "내용1");
        postService.write(member4, "제목2", "내용2");
    }

    // 조회
    @Transactional(readOnly = true)
    void work2() {
        Optional<Post> opPost = postService.getPost(1);
        // select * from post where id = 1;
    }

    // 삭제
    @Transactional
    void work3() {
        Post post1 = postService.getPost(1).get();
        Post post2 = postService.getPost(2).get();

        postService.delete(post1);
        if(true)
            throw new RuntimeException("테스트용 예외 발생");
        postService.delete(post2);
    }

    // 수정
    @Transactional
    void work4() {
        Post post1 = postService.getPost(1).get();
        postService.modify(post1,"제목1-수정2", "내용1-수정2");
    }
}
