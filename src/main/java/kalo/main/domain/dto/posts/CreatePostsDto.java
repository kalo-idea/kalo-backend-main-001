package kalo.main.domain.dto.posts;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatePostsDto {
    String title = "";

    String content = "";

    String photos = "";

    Long usersId;

    String topic = "";

    List<String> hashtags = new ArrayList<String>();

    String addressName = "";

    String region1depthName = "전국 단위";

    String region2depthName = "";
    
    String region3depthName = "";

    Double latitude = 37.53362220676191d;

    Double longitude = 126.97755295059169d;

    @Builder
    public CreatePostsDto(String title, String content, String photos, Long usersId, String topic, List<String> hashtags, String addressName, String region1depthName, String region2depthName, String region3depthName, Double latitude, Double longitude) {
        this.title = title;
        this.content = content;
        this.photos = photos;
        this.usersId = usersId;
        this.topic = topic;
        this.hashtags = hashtags;
        this.addressName = addressName;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.region3depthName = region3depthName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
