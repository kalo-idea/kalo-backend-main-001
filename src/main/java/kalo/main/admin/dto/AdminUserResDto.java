package kalo.main.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserResDto {
    Long id;
    String type;
    String nickname;
    String intro;
    String profileSrc;
    String publicInfos;
    Long ledger;
}
