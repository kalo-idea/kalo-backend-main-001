package kalo.main.domain.dto;

import kalo.main.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleWriterDto {
    Long userId;
    String nickname;
    String profileSrc;

    public SimpleWriterDto(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profileSrc = user.getProfileSrc();
    }
}
