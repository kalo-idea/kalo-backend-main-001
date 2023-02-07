package kalo.main.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminModifyPostReqDto {
    Long petitionId;
    String title;
    String content;
    String hashtags;
    String photos;
    String region1depthName;
    String region2depthName;
}
