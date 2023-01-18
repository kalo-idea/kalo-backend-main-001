package kalo.main.domain.dto.post;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadSimplePostDto {
    
    Long postId;

    Long writerId;
    
    String title;

    LocalDateTime createdDate;

    String content;

    Long likeCount;

    Long dislikeCount;

    Long replyCount;

    String topic;

    String region1depthName;

    String region2depthName;

    @Builder
    @QueryProjection
    public ReadSimplePostDto(Long postId, Long writerId, String title, LocalDateTime createdDate, String content, Long likeCount, Long dislikeCount, Long replyCount, String topic, String region1depthName, String region2depthName) {
        this.postId = postId;
        this.writerId = writerId;
        this.title = title;
        this.createdDate = createdDate;
        this.content = content;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.replyCount = replyCount;
        this.topic = topic;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
    }
}
