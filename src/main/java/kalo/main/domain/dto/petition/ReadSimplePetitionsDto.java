package kalo.main.domain.dto.petition;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadSimplePetitionsDto {
    
    Long petitionId;

    Long writerId;
    
    String title;

    LocalDateTime createdDate;

    String content;

    Long likeCount;

    Long dislikeCount;

    String progress;

    String step;

    Long goal;

    Long replyCount;

    String category;

    String region1depthName;

    String region2depthName;

    Double latitude;

    Double longitude;

    Long supportCount;

    LocalDateTime supportingDateEnd;

    @Builder
    @QueryProjection
    public ReadSimplePetitionsDto(Long petitionId, Long writerId, String title, LocalDateTime createdDate, String content, Long likeCount, Long dislikeCount, String progress, String step, Long goal, Long replyCount, String category, String region1depthName, String region2depthName, Double latitude, Double longitude, Long supportCount, LocalDateTime supportingDateEnd) {
        this.petitionId = petitionId;
        this.writerId = writerId;
        this.title = title;
        this.createdDate = createdDate;
        this.content = content;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
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
        this.supportingDateEnd = supportingDateEnd;
    }
}
