package kalo.main.domain.dto.user;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserInfoReqDto {
    Long authId;
    String tel;
    String region1depthName;
    String region2depthName;
    Boolean promotionCheck;
}
