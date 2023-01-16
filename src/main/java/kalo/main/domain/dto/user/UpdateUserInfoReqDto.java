package kalo.main.domain.dto.user;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserInfoReqDto {
    Long userId;
    String name;
    String nickname;
    LocalDate birth;
    String gender;
    String email;
    String tel;
    String region1depthName;
    String region2depthName;
    Boolean promotionCheck;

}
