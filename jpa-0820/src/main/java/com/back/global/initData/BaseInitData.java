package com.back.global.initData;


import com.back.domain.post.entity.Post;
import com.back.domain.post.service.PostService;
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

    @Bean
    ApplicationRunner initDataRunner() {
        return args -> {
            self.work1();
            self.work2();

//            new Thread(() -> {
//                self.work3();
//            }).start();

            self.work4();
        };
    }

    // 생성
    @Transactional
    void work1() {
        if(postService.getTotalCount() > 0) {
            return;
        }
        // 서비스를 도입해서 비즈니스 로직을 재사용
        postService.write("제목1", "내용1");
        postService.write("제목2", "내용2");
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
        postService.modify(post1,"제목1-수정3", "내용1-수정2");
    }
}
