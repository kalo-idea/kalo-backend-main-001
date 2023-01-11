package kalo.main.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyDto {
    
    Long commentId;

    Long userId;
    String nickname;
    String profileSrc;

    Boolean isLike;
    Long likeCount;
    Boolean isDislike;
    Long dislikeCount;

    String content;

    @QueryProjection
    @Builder
    public ReplyDto(Long commentId, Long userId, String nickname, String profileSrc, Boolean isLike, Long likeCount, Boolean isDislike, Long dislikeCount, String content) {
        this.commentId = commentId;
        this.userId = userId;
        this.nickname = nickname;
        this.profileSrc = profileSrc;
        this.isLike = isLike;
        this.likeCount = likeCount;
        this.isDislike = isDislike;
        this.dislikeCount = dislikeCount;
        this.content = content;
    }
    

}
