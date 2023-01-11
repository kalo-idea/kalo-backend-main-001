package kalo.main.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LikeDislikeResDto {
    
    Long id;
    Boolean isLike;
    Long likeCount;
    Boolean isDislike;
    Long dislikeCount;
}
