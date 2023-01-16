package kalo.main.repository;

import static kalo.main.domain.QAuth.auth;
import static kalo.main.domain.QUser.user;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kalo.main.domain.dto.user.UserAuthResDto;
import kalo.main.domain.dto.user.QUserAuthResDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    
    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserAuthResDto> getAuthKakao(String kakao) {
        return queryFactory.select(new QUserAuthResDto(
            auth.id,
            user.id,
            auth.type,
            auth.kakao,
            auth.email,
            auth.name,
            auth.gender,
            auth.tel,
            auth.address,
            auth.region1depthName,
            auth.region2depthName,
            auth.promotionCheck,
            auth.fcmToken,
            auth.recentLogin,
            user.type,
            user.nickname,
            user.intro,
            user.profileSrc,
            user.publicInfos
        ))
        .from(user)
        .join(user.auth, auth)
        .where(auth.kakao.eq(kakao))
        .fetch();
    }
}
