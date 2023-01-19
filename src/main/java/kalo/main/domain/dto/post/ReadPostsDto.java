package kalo.main.domain.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import kalo.main.domain.dto.SimpleWriterDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadPostsDto {
    
    Long postId;

    SimpleWriterDto writer;
    
    String title;

    LocalDateTime createdDate;

    String content;

    List<String> hashtags;

    List<String> media;

    Long likeCount;

    Long dislikeCount;

    Long replyCount;

    String topic;

    String region1depthName;

    String region2depthName;

    @Builder
    @QueryProjection
    public ReadPostsDto(ReadSimplePostDto readSimplePostDto, SimpleWriterDto writer, List<String> hashtags, List<String> media) {
        this.postId = readSimplePostDto.getPostId();
        this.writer = writer;
        this.title = readSimplePostDto.getTitle();
        this.createdDate = readSimplePostDto.getCreatedDate();
        this.content = readSimplePostDto.getContent();
        this.hashtags = hashtags;
        this.media = media;
        this.likeCount = readSimplePostDto.getLikeCount();
        this.dislikeCount = readSimplePostDto.getDislikeCount();
        this.replyCount = readSimplePostDto.getReplyCount();
        this.topic = readSimplePostDto.getTopic();
        this.region1depthName = readSimplePostDto.getRegion1depthName();
        this.region2depthName = readSimplePostDto.getRegion2depthName();
    }
}
