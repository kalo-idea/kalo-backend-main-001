package kalo.main.repository;

import static kalo.main.domain.QHashtag.hashtag;
import static kalo.main.domain.QPostHashtag.postHashtag;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kalo.main.domain.Hashtag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HashtagRepositoryImpl implements HashtagRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Hashtag> findPostHashtags(Long postId) {
        return queryFactory.select(hashtag).from(postHashtag).join(postHashtag.hashtag, hashtag).where(postHashtag.post.id.eq(postId)).fetch();
    }
}
