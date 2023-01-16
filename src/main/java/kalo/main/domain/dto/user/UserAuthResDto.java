package kalo.main.domain.dto.user;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAuthResDto {
    Long authId;
    Long userId;

    String authType;
    String kakao;
    String email;
    String name;
    String gender;
    String tel;
    String address;
    String region1depthName;
    String region2depthName;
    Boolean promotionCheck;
    String fcmToken;
    LocalDateTime recentLogin;

    String userType;
    String nickname;
    String intro;
    String profileSrc;
    String publicInfos;

    @QueryProjection
    public UserAuthResDto(Long authId, Long userId, String authType, String kakao, String email, String name, String gender, String tel, String address, String region1depthName, String region2depthName, Boolean promotionCheck, String fcmToken, LocalDateTime recentLogin, String userType, String nickname, String intro, String profileSrc, String publicInfos) {
        this.authId = authId;
        this.userId = userId;
        this.authType = authType;
        this.kakao = kakao;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.tel = tel;
        this.address = address;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.promotionCheck = promotionCheck;
        this.fcmToken = fcmToken;
        this.recentLogin = recentLogin;
        this.userType = userType;
        this.nickname = nickname;
        this.intro = intro;
        this.profileSrc = profileSrc;
        this.publicInfos = publicInfos;
    }


    // public UserAuthResDto(Auth auth, User user) {
    //     this.authId = auth.getId();
    //     this.userId = user.getId();
    //     this.authType = auth.getType();
    //     this.kakao = auth.getKakao();
    //     this.email = auth.getEmail();
    //     this.name = auth.getName();
    //     this.gender = auth.getGender();
    //     this.tel = auth.getTel();
    //     this.address = auth.getAddress();
    //     this.region1depthName = auth.getRegion1depthName();
    //     this.region2depthName = auth.getRegion2depthName();
    //     this.promotionCheck = auth.getPromotionCheck();
    //     this.fcmToken = auth.getFcmToken();
    //     this.recentLogin = auth.getRecentLogin();
    //     this.userType = user.getType();
    //     this.nickname = user.getNickname();
    //     this.intro = user.getIntro();
    //     this.profileSrc = user.getProfileSrc();
    //     this.publicInfos = user.getPublicInfos();
    // }
}
