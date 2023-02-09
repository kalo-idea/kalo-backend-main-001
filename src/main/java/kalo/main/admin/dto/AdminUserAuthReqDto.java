package kalo.main.admin.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserAuthReqDto {
    Long userId;

    Boolean userType;
    Boolean nickname;
    Boolean intro;
    Boolean profileSrc;
    Boolean publicInfos;
    Boolean ledger;
    Boolean recentLogin;
    
    Boolean authType;
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
}
