package kalo.main.domain.dto.post;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostCondDto {
    String region1depthName;
    String region2depthName;
    String topic;
    Long userId;

    public PostCondDto(String region1depthName, String region2depthName, String topic, Long userId) {
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.topic = topic;
        this.userId = userId;
    }
}
