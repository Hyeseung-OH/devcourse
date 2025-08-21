package com.back.global.initData;


import com.back.domain.post.entity.Post;
import com.back.domain.post.repository.PostRepository;
import com.back.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    private final PostService postService;

    @Bean
    @Transactional
    ApplicationRunner initDataRunner() {
        return args -> {
            work1();
            work2();
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
    @Transactional
    void work2() {
        Optional<Post> opPost = postService.getPost(1);
        // select * from post where id = 1;
    }

}
