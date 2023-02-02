package kalo.main.domain.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserInfoReqDto {
    Long id;
    String tel;
    String region1depthName;
    String region2depthName;
    Boolean promotionCheck;
}
