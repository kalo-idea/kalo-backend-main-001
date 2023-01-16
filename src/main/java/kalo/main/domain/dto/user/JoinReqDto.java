package kalo.main.domain.dto.user;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinReqDto {
    LocalDate birth;
    String email;
    String fcmToken;
    String gender;
    String kakao;
    String name;
    Boolean promotionCheck;
    String region1depthName;
    String region2depthName;
    String tel;

    String nickname;
}
