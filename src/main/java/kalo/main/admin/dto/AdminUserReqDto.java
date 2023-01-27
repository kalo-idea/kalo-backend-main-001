package kalo.main.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserReqDto {
    Long userId;
    Boolean type;
    Boolean nickname;
    Boolean intro;
    Boolean profileSrc;
    Boolean publicInfos;
    Boolean ledger;
}
