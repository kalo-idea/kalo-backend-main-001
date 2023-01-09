package kalo.main.repository;

import static kalo.main.domain.QPost.post;
import static kalo.main.domain.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kalo.main.domain.dto.post.GetPostWriter;
import kalo.main.domain.dto.post.QGetPostWriter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public GetPostWriter findWriter(Long postId) {
        return queryFactory.select(new QGetPostWriter(
            user.id,
            user.nickname,
            user.profileSrc
            )
        )
        .from(post)
        .join(post.user, user)
        .where(post.id.eq(postId))
        .fetchOne();
    }
}
