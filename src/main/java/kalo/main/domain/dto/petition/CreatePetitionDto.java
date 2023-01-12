package kalo.main.domain.dto.petition;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatePetitionDto {
    String title = "";

    String content = "";

    String photos = "";

    Long userId;

    List<String> hashtags = new ArrayList<String>();

    String addressName = "";

    String region1depthName = "전국 단위";

    String region2depthName = "";
    
    String region3depthName = "";

    Double latitude = 37.53362220676191d;

    Double longitude = 126.97755295059169d;

    String category;

    Long goal;
}
