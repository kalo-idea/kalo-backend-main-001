package kalo.main.domain.dto.posts;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class GetPostsWriter {

    Long userId;
    String nickname;
    String profileSrc;

    @QueryProjection
    public GetPostsWriter(Long userId, String nickname, String profileSrc) {
        this.userId = userId;
        this.nickname = nickname;        
        this.profileSrc = profileSrc;
    }
}
