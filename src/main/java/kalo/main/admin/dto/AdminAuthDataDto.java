package kalo.main.admin.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminAuthDataDto {
    Long id;
    String type;
    String kakao;
    String email;
    String name;
    LocalDate birth;
    String gender;
    String tel;
    String address;
    String region1depthName;
    String region2depthName;
    Boolean promotionCheck;
    String fcmToken;
    LocalDateTime recentLogin;
    LocalDateTime createdDate;
}
