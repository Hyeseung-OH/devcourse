package com.back.domain.post.repository;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("2번 글 조회")
    void t1() {
        Post post1 = postRepository.findById(2).get();

        assertThat(post1.getId()).isEqualTo(2);
        assertThat(post1.getTitle()).isEqualTo("제목2");
        assertThat(post1.getContent()).isEqualTo("내용2");
    }

    @Test
    @DisplayName("글 생성")
    void t2() {
        Post newPost = new Post("new 제목", "new 내용", null);
        assertThat(newPost.getId()).isNull();

        Post savedPost = postRepository.save(newPost);

        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("new 제목");
        assertThat(savedPost.getContent()).isEqualTo("new 내용");
    }

    @Test
    @DisplayName("글 개수 조회")
    void t3() {
        long cnt = postRepository.count();
        assertThat(cnt).isEqualTo(2L);
    }
}
