package kalo.main.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PostReply extends BaseEntity {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "TEXT")
    String content;

    Long likeCount = 0L;

    Long dislikeCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Builder
    public PostReply(Long id, String content, Long likeCount, Long dislikeCount, Post post, User user) {
        this.id = id;
        this.content = content;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.post = post;
        this.user = user;
    }
}
