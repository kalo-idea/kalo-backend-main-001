package kalo.main.repository;

import static kalo.main.domain.QHashtag.hashtag;
import static kalo.main.domain.QPost.post;
import static kalo.main.domain.QPetition.petition;
import static kalo.main.domain.QPostHashtag.postHashtag;
import static kalo.main.domain.QPetitionHashtag.petitionHashtag;

import java.util.List;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import kalo.main.domain.Hashtag;
import kalo.main.domain.Petition;
import kalo.main.domain.Post;
import kalo.main.domain.dto.petition.QReadSimplePetitionsDto;
import kalo.main.domain.dto.petition.ReadSimplePetitionsDto;
import kalo.main.domain.dto.post.QReadSimplePostDto;
import kalo.main.domain.dto.post.ReadSimplePostDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HashtagRepositoryImpl implements HashtagRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Hashtag> findPostHashtags(Long postId) {
        return queryFactory.select(hashtag).from(postHashtag).join(postHashtag.hashtag, hashtag).where(postHashtag.post.deleted.eq(false), postHashtag.post.id.eq(postId)).fetch();
    }

    @Override
    public List<Hashtag> findPetitionHashtags(Long petitionId) {
        return queryFactory.select(hashtag).from(petitionHashtag).join(petitionHashtag.hashtag, hashtag).where(petitionHashtag.petition.deleted.eq(false), petitionHashtag.petition.id.eq(petitionId)).fetch();
    }
    
    @Override
    public List<ReadSimplePetitionsDto> getPetitionsByHashtag(Pageable pageable, String hash) {
        JPAQuery<ReadSimplePetitionsDto> query =  queryFactory
            .select(new QReadSimplePetitionsDto(
                petition.id,
                petition.user.id,
                petition.title,
                petition.createdDate,
                petition.content.substring(0, 100),
                petition.likeCount,
                petition.dislikeCount,
                petition.progress,
                petition.step,
                petition.goal,
                petition.replyCount,
                petition.category,
                petition.region1depthName,
                petition.region2depthName,
                petition.latitude,
                petition.longitude,
                petition.supportCount,
                petition.supportingDateEnd
                )
            )
            .from(petitionHashtag)
            .join(petitionHashtag.hashtag, hashtag)
            .join(petitionHashtag.petition, petition)
            .where(
                hashtag.word.eq(hash),
                petition.deleted.eq(false)    
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

            for (Sort.Order o : pageable.getSort()) {
                PathBuilder pathBuilder = new PathBuilder(petition.getType(), petition.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }

            List<ReadSimplePetitionsDto> result = query.fetch();

            return result;
    }

    @Override
    public List<ReadSimplePostDto> getPostByHashtag(Pageable pageable, String hash) {
        JPAQuery<ReadSimplePostDto> query = queryFactory
            .select(new QReadSimplePostDto(
                post.id,
                post.user.id,
                post.title,
                post.createdDate,
                post.content.substring(0, 100),
                post.likeCount,
                post.dislikeCount,
                post.replyCount, 
                post.topic, 
                post.region1depthName,
                post.region2depthName,
                post.latitude,
                post.longitude
                )
            )
            .from(postHashtag)
            .join(postHashtag.hashtag, hashtag)
            .join(postHashtag.post, post)
            .where(
                hashtag.word.eq(hash),
                post.deleted.eq(false)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

            for (Sort.Order o : pageable.getSort()) {
                PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
            List<ReadSimplePostDto> result = query.fetch();
            return result;
    }
}
