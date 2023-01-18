package kalo.main.domain.dto.petition;

import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import kalo.main.domain.dto.SimpleWriterDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadPetitionsDto {
    
    Long petitionId;

    SimpleWriterDto writer;
    
    String title;

    LocalDateTime createdDate;

    String content;

    List<String> hashtags;

    List<String> photos;

    Long likeCount;

    Long dislikeCount;

    String progress;

    Long goal;

    Long replyCount;

    String category;

    String region1depthName;

    String region2depthName;

    Long supportCount;

    @Builder
    @QueryProjection
    public ReadPetitionsDto(ReadSimplePetitionsDto readSimplePetitionsDto, SimpleWriterDto writer, List<String> hashtags, List<String> photos) {
        this.petitionId = readSimplePetitionsDto.getPetitionId();
        this.writer = writer;
        this.title = readSimplePetitionsDto.getTitle();
        this.createdDate = readSimplePetitionsDto.getCreatedDate();
        this.content = readSimplePetitionsDto.getContent();
        this.hashtags = hashtags;
        this.photos = photos;
        this.likeCount = readSimplePetitionsDto.getLikeCount();
        this.dislikeCount = readSimplePetitionsDto.getDislikeCount();
        this.progress = readSimplePetitionsDto.getProgress();
        this.goal = readSimplePetitionsDto.getGoal();
        this.replyCount = readSimplePetitionsDto.getReplyCount();
        this.category = readSimplePetitionsDto.getCategory();
        this.region1depthName = readSimplePetitionsDto.getRegion1depthName();
        this.region2depthName = readSimplePetitionsDto.getRegion2depthName();
        this.supportCount = readSimplePetitionsDto.getSupportCount();
    }
}
