package kalo.main.repository;

import static kalo.main.domain.QHashtags.hashtags;
import static kalo.main.domain.QPostsHashtags.postsHashtags;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kalo.main.domain.Hashtags;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HashtagsRepositoryImpl implements HashtagsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Hashtags> findPostsHashtags(Long postId) {
        return queryFactory.select(hashtags).from(postsHashtags).join(postsHashtags.hashtags, hashtags).where(postsHashtags.posts.id.eq(postId)).fetch();
    }
}
