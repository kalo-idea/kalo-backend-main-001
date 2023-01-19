package kalo.main.domain.dto.user;

import kalo.main.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDto {
    Long userId;
    String type;
    String nickname;
    String intro;
    String profileSrc;
    String publicInfos;


    public UserInfoDto(User user) {
        this.userId = user.getId();
        this.type = user.getType();
        this.nickname = user.getNickname();
        this.intro = user.getIntro();
        this.profileSrc = user.getProfileSrc();
        this.publicInfos = user.getPublicInfos();
    }
}
