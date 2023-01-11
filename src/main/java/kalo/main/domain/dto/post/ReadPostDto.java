package kalo.main.domain.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ReadPostDto{

    Long userId;
    String nickname;
    String profileSrc;
    
    String title;

    LocalDateTime createdDate;

    String content;

    List<String> hashtags;

    String photos;

    Long likeCount;

    Boolean isLike;

    Long dislikeCount;

    Boolean isDislike;

    Long replyCount;

    String topic;

    String region1depthName;

    String region2depthName;

    @QueryProjection
    public ReadPostDto(Long userId, String nickname, String profileSrc, String title, LocalDateTime createdDate, String content, List<String> hashtags, String photos, Long likeCount, Boolean isLike, Long dislikeCount, Boolean isDislike, Long replyCount, String topic, String region1depthName, String region2depthName) {
        this.userId = userId;
        this.nickname = nickname;        
        this.profileSrc = profileSrc;
        this.title = title;
        this.createdDate = createdDate;
        this.content = content;
        this.hashtags = hashtags;
        this.photos = photos;
        this.likeCount = likeCount;
        this.isLike = isLike;
        this.dislikeCount = dislikeCount;
        this.isDislike = isDislike;
        this.replyCount = replyCount;
        this.topic = topic;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
    }
    


}
