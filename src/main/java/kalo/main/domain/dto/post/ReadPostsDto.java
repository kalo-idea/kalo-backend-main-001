package kalo.main.domain.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadPostsDto {
    
    Long postId;

    Long userId;
    String nickname;
    String profileSrc;
    
    String title;

    LocalDateTime createdDate;

    String content;

    List<String> hashtags;

    String photos;

    Long likeCount;

    Long dislikeCount;

    String progress;

    Long goal;

    Long replyCount;

    String topic;

    String region1depthName;

    String region2depthName;

    @Builder
    @QueryProjection
    public ReadPostsDto(ReadSimplePostDto readSimplePostDto, Long userId, String nickname, String profileSrc, List<String> hashtags) {
        this.postId = readSimplePostDto.getPostId();
        this.userId = userId;
        this.nickname = nickname;
        this.profileSrc = profileSrc;
        this.title = readSimplePostDto.getTitle();
        this.createdDate = readSimplePostDto.getCreatedDate();
        this.content = readSimplePostDto.getContent();
        this.hashtags = hashtags;
        this.photos = readSimplePostDto.getPhotos();
        this.likeCount = readSimplePostDto.getLikeCount();
        this.dislikeCount = readSimplePostDto.getDislikeCount();
        this.progress = readSimplePostDto.getProgress();
        this.goal = readSimplePostDto.getGoal();
        this.replyCount = readSimplePostDto.getReplyCount();
        this.topic = readSimplePostDto.getTopic();
        this.region1depthName = readSimplePostDto.getRegion1depthName();
        this.region2depthName = readSimplePostDto.getRegion2depthName();
    }
}
