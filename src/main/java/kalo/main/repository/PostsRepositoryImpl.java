package kalo.main.repository;

import static kalo.main.domain.QPosts.posts;
import static kalo.main.domain.QUsers.users;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kalo.main.domain.dto.posts.GetPostsWriter;
import kalo.main.domain.dto.posts.QGetPostsWriter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostsRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public GetPostsWriter findWriter(Long postId) {
        return queryFactory.select(new QGetPostsWriter(
            users.id,
            users.nickname,
            users.profileSrc
            )
        )
        .from(posts)
        .join(posts.users, users)
        .where(posts.id.eq(postId))
        .fetchOne();
    }
}
