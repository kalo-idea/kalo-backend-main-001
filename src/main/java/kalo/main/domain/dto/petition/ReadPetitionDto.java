package kalo.main.domain.dto.petition;

import java.time.LocalDateTime;
import java.util.List;

import kalo.main.domain.dto.SimpleWriterDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadPetitionDto {
    
    SimpleWriterDto writer;

    Long id;
    
    String title;

    LocalDateTime createdDate;

    String content;

    List<String> hashtags;

    List<String> media;

    Long likeCount;

    Boolean isLike;

    Long dislikeCount;

    Boolean isDislike;

    String progress;

    List<String> step;

    Long goal;

    Long replyCount;

    String category;

    String region1depthName;

    String region2depthName;

    Long supportCount;

    Boolean isSupport;

    Double latitude;

    Double longitude;

    @Builder
    public ReadPetitionDto(SimpleWriterDto writer, Long id, String title, LocalDateTime createdDate, String content, List<String> hashtags, List<String> media, Long likeCount, Boolean isLike, Long dislikeCount, Boolean isDislike, String progress, List<String> step, Long goal, Long replyCount, String category, String region1depthName, String region2depthName, Double latitude, Double longitude, Long supportCount, Boolean isSupport) {
        this.writer = writer;
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
        this.content = content;
        this.hashtags = hashtags;
        this.media = media;
        this.likeCount = likeCount;
        this.isLike = isLike;
        this.dislikeCount = dislikeCount;
        this.isDislike = isDislike;
        this.progress = progress;
        this.step = step;
        this.goal = goal;
        this.replyCount = replyCount;
        this.category = category;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.supportCount = supportCount;
        this.isSupport = isSupport;
    }
    
}
