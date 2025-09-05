package com.spring3.domain.post.post.entity;

import com.spring3.domain.post.comment.entity.Comment;
import com.spring3.global.jpa.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Entity
public class Post extends BaseEntity {
    private String title;
    private String content;

    /**
     * Post와 Comment의 일대다 관계 설정
     *
     * @OneToMany 설정 옵션들:
     * - mappedBy = "post": Comment 엔티티의 post 필드가 외래키를 관리 (연관관계의 주인은 Comment)
     * - cascade = {CascadeType.PERSIST, CascadeType.REMOVE}:
     *   └ PERSIST: Post 저장시 연관된 Comment들도 함께 저장
     *   └ REMOVE: Post 삭제시 연관된 Comment들도 함께 삭제
     * - orphanRemoval = true: 부모(Post)와 연결이 끊어진 Comment는 자동 삭제
     * - fetch = FetchType.LAZY: Comment 목록은 실제 접근할 때 지연 로딩
     *
     * ※ 주의: Cascade 동작은 트랜잭션 내에서만 작동함
     */
    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval=true, fetch = FetchType.LAZY) // Comment.post 필드에 의해 매핑됨
    // 비어 있는 값을 넣어 주지 않으면 NullPointerException이 발생할 수 있음
    private List<Comment> comments = new ArrayList<>();

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void deleteComment(Long commentId) {
        Comment comment = findCommentById(commentId).get();
        this.comments.remove(comment);
    }

    public Comment updateComment(Long commentId, String content) {
        Comment comment = findCommentById(commentId).get();
        comment.update(content);
        return comment;
    }

    public Optional<Comment> findCommentById(Long commentId) {
        return comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst();
    }

    public Comment addComment(String content) {
        Comment comment = new Comment(content, this);
        this.comments.add(comment);

        return comment;
    }
}
