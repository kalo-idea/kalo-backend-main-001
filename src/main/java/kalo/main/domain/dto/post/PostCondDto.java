package kalo.main.domain.dto.post;

import lombok.Data;

@Data
public class PostCondDto {
    String region1depthName;
    String region2depthName;
    String topic;

    public PostCondDto() {
    }

    public PostCondDto(String region1depthName, String region2depthName, String topic) {
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.topic = topic;
    }
}
