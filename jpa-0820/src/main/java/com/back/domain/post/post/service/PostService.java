package com.back.domain.post.post.service;

import com.back.domain.post.member.entity.Member;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public void write(Member author, String title, String content) {
        Post post = new Post(title, content, author);
        postRepository.save(post);
    }

    public void modify(Post post, String title, String content) {
        post.setTitle(title);
        post.setContent(content);

        //postRepository.save(post);
    }

    public Optional<Post> getPost(int id) {
        return postRepository.findById(id);
    }

    public long getTotalCount() {
        return postRepository.count();
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }
}
