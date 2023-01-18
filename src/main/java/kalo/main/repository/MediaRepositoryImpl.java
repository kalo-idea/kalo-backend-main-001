package kalo.main.repository;

import static kalo.main.domain.QMedia.media;
import static kalo.main.domain.QMediaPetition.mediaPetition;
import static kalo.main.domain.QMediaPost.mediaPost;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kalo.main.domain.Media;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaRepositoryImpl implements MediaRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Media> findPostMedia(Long postId) {
        return queryFactory.select(media).from(mediaPost).join(mediaPost.media, media).where(mediaPost.post.deleted.eq(false), mediaPost.post.id.eq(postId)).fetch();
    }

    @Override
    public List<Media> findPetitionMedia(Long petitionId) {
        return queryFactory.select(media).from(mediaPetition).join(mediaPetition.media, media).where(mediaPetition.petition.deleted.eq(false), mediaPetition.petition.id.eq(petitionId)).fetch();
    }
}
