package kalo.main.domain.dto.user;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserProfileReqDto {

    Map<String, Boolean> check;
    String intro;
    String profile;
    Long userId;
}
