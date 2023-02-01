package kalo.main.domain.dto.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAuthResDto {
    Long authId;

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
    List<UserInfoDto> userInfos;

    @QueryProjection
    @Builder
    public UserAuthResDto(Long authId, String type, String kakao, String email, String name, LocalDate birth, String gender, String tel, String address, String region1depthName, String region2depthName, Boolean promotionCheck, String fcmToken, LocalDateTime recentLogin, List<UserInfoDto> userInfos) {
        this.authId = authId;
        this.type = type;
        this.kakao = kakao;
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.tel = tel;
        this.address = address;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.promotionCheck = promotionCheck;
        this.fcmToken = fcmToken;
        this.recentLogin = recentLogin;
        this.userInfos = userInfos;
    }
}
