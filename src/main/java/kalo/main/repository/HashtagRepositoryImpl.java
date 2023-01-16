package kalo.main.repository;

import static kalo.main.domain.QHashtag.hashtag;
import static kalo.main.domain.QPostHashtag.postHashtag;
import static kalo.main.domain.QPetitionHashtag.petitionHashtag;
import static kalo.main.domain.QPost.post;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kalo.main.domain.Hashtag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HashtagRepositoryImpl implements HashtagRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Hashtag> findPostHashtags(Long postId) {
        return queryFactory.select(hashtag).from(postHashtag).join(postHashtag.hashtag, hashtag).join(postHashtag.post, post).where(postHashtag.post.deleted.eq(false), postHashtag.post.id.eq(postId)).fetch();
    }

    @Override
    public List<Hashtag> findPetitionHashtags(Long petitionId) {
        return queryFactory.select(hashtag).from(petitionHashtag).join(petitionHashtag.hashtag, hashtag).where(petitionHashtag.petition.deleted.eq(false), petitionHashtag.petition.id.eq(petitionId)).fetch();
    }
}
