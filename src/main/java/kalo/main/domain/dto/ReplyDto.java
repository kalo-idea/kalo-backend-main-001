package kalo.main.domain.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyDto {
    
    Long id;

    SimpleWriterDto writer;
    LocalDateTime createdDate;

    Boolean isLike;
    Long likeCount;

    Boolean isDislike;
    Long dislikeCount;

    String content;

    @QueryProjection
    @Builder
    public ReplyDto(Long id, SimpleWriterDto writer, LocalDateTime createdDate,  Boolean isLike, Long likeCount, Boolean isDislike, Long dislikeCount, String content) {
        this.id = id;
        this.writer = writer;
        this.createdDate = createdDate;
        this.isLike = isLike;
        this.likeCount = likeCount;
        this.isDislike = isDislike;
        this.dislikeCount = dislikeCount;
        this.content = content;
    }
    

}
