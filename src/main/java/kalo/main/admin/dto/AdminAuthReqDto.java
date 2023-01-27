package kalo.main.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminAuthReqDto {

    Long authId;
    Boolean type;
    Boolean kakao;
    Boolean email;
    Boolean name;
    Boolean birth;
    Boolean gender;
    Boolean tel;
    Boolean address;
    Boolean region1depthName;
    Boolean region2depthName;
    Boolean promotionCheck;
    Boolean fcmToken;
    Boolean recentLogin;
}
