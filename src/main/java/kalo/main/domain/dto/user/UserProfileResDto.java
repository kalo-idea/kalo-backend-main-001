package kalo.main.domain.dto.user;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileResDto {
    Long userId;

    String nickname;

    String intro;

    LocalDate birth;
    String email;
    String gender;
    String name;
    String region1depthName;
    String region2depthName;
    String tel;
    String profileSrc;
    Boolean promotionCheck;
}
