package kalo.main.domain.dto.post;

public class PostCondDto {
    String sort;
    String region1depthName;
    String region2depthName;
    String topic;
    String progress;

    public PostCondDto() {
    }

    public PostCondDto(String sort, String region1depthName, String region2depthName, String topic) {
        this.sort = sort;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.topic = topic;
    }
}
