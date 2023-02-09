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
@Builder
public class ReadPostDto{

    Long id;

    SimpleWriterDto writer;
    
    String title;

    LocalDateTime createdDate;

    String content;

    List<String> hashtags;

    List<String> medium;

    Long likeCount;

    Boolean isLike;

    Long dislikeCount;

    Boolean isDislike;

    Long replyCount;

    String topic;

    String region1depthName;

    String region2depthName;

    Double latitude;

    Double longitude;

    @QueryProjection
    public ReadPostDto(Long id, SimpleWriterDto writer, String title, LocalDateTime createdDate, String content, List<String> hashtags, List<String> medium, Long likeCount, Boolean isLike, Long dislikeCount, Boolean isDislike, Long replyCount, String topic, String region1depthName, String region2depthName, Double latitude, Double longitude) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.createdDate = createdDate;
        this.content = content;
        this.hashtags = hashtags;
        this.medium = medium;
        this.likeCount = likeCount;
        this.isLike = isLike;
        this.dislikeCount = dislikeCount;
        this.isDislike = isDislike;
        this.replyCount = replyCount;
        this.topic = topic;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    


}
