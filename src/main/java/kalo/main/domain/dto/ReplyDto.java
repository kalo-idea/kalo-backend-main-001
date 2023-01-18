package kalo.main.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyDto {
    
    Long commentId;

    SimpleWriterDto writer;

    Boolean isLike;
    Long likeCount;
    Boolean isDislike;
    Long dislikeCount;

    String content;

    @QueryProjection
    @Builder
    public ReplyDto(Long commentId, SimpleWriterDto writer, Boolean isLike, Long likeCount, Boolean isDislike, Long dislikeCount, String content) {
        this.commentId = commentId;
        this.writer = writer;
        this.isLike = isLike;
        this.likeCount = likeCount;
        this.isDislike = isDislike;
        this.dislikeCount = dislikeCount;
        this.content = content;
    }
    

}
