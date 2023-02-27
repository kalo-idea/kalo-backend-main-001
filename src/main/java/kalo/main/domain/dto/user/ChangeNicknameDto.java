package kalo.main.domain.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeNicknameDto {
    Long id;
    String nickname;
}
