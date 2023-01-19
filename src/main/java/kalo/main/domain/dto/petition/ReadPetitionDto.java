package kalo.main.domain.dto.petition;

import java.time.LocalDateTime;
import java.util.List;

import kalo.main.domain.dto.SimpleWriterDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ReadPetitionDto {
    
    SimpleWriterDto writer;
    
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

    Long goal;

    Long replyCount;

    String category;

    String region1depthName;

    String region2depthName;

    Long supportCount;

    Boolean isSupport;

    public ReadPetitionDto(SimpleWriterDto writer, String title, LocalDateTime createdDate, String content, List<String> hashtags, List<String> media, Long likeCount, Boolean isLike, Long dislikeCount, Boolean isDislike, String progress, Long goal, Long replyCount, String category, String region1depthName, String region2depthName, Long supportCount, Boolean isSupport) {
        this.writer = writer;
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
        this.goal = goal;
        this.replyCount = replyCount;
        this.category = category;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.supportCount = supportCount;
        this.isSupport = isSupport;
    }
    
}
