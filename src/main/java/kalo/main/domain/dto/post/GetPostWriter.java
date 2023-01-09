package kalo.main.domain.dto.post;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class GetPostWriter {

    Long userId;
    String nickname;
    String profileSrc;

    @QueryProjection
    public GetPostWriter(Long userId, String nickname, String profileSrc) {
        this.userId = userId;
        this.nickname = nickname;        
        this.profileSrc = profileSrc;
    }
}
