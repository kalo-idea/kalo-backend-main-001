package kalo.main.domain.dto.petition;

import java.time.LocalDateTime;
import java.util.List;

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

    String photos;

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
    public ReadSimplePetitionsDto(Long petitionId, Long writerId, String title, LocalDateTime createdDate, String content, String photos, Long likeCount, Long dislikeCount, String progress, Long goal, Long replyCount, String category, String region1depthName, String region2depthName, Long supportCount) {
        this.petitionId = petitionId;
        this.writerId = writerId;
        this.title = title;
        this.createdDate = createdDate;
        this.content = content;
        this.photos = photos;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.progress = progress;
        this.goal = goal;
        this.replyCount = replyCount;
        this.category = category;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.supportCount = supportCount;
    }
}
