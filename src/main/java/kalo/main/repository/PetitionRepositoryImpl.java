package kalo.main.repository;

import static kalo.main.domain.QHashtag.hashtag;
import static kalo.main.domain.QLikePetition.likePetition;
import static kalo.main.domain.QPetition.petition;
import static kalo.main.domain.QPetitionHashtag.petitionHashtag;
import static kalo.main.domain.QSupportPetition.supportPetition;
import static kalo.main.domain.QUser.user;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kalo.main.domain.QUser;
import kalo.main.domain.dto.petition.PetitionCondDto;
import kalo.main.domain.dto.petition.QReadSimplePetitionsDto;
import kalo.main.domain.dto.petition.ReadSimplePetitionsDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PetitionRepositoryImpl implements PetitionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReadSimplePetitionsDto> findListPetitions(Pageable pageable, PetitionCondDto cond) {

        JPAQuery<ReadSimplePetitionsDto> query = queryFactory.select(new QReadSimplePetitionsDto(
        petition.id,
        petition.user.id,
        petition.title,
        petition.createdDate,
        petition.content.substring(0, 100),
        petition.photos,
        petition.likeCount,
        petition.dislikeCount,
        petition.progress,
        petition.goal,
        petition.replyCount,
        petition.category,
        petition.region1depthName,
        petition.region2depthName,
        petition.supportCount))
        .from(petition)
        .where(
            petition.deleted.eq(false),
            region1Filter(cond.getRegion1depthName()),
            region2Filter(cond.getRegion2depthName()),
            progressFilter(cond.getProgress()),
            categoryFilter(cond.getCategory())
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
    public List<ReadSimplePetitionsDto> findLikePetitions(Pageable pageable, Long viewerId) {

        JPAQuery<ReadSimplePetitionsDto> query = queryFactory.select(new QReadSimplePetitionsDto(
        petition.id,
        petition.user.id,
        petition.title,
        petition.createdDate,
        petition.content,
        petition.photos,
        petition.likeCount,
        petition.dislikeCount,
        petition.progress,
        petition.goal,
        petition.replyCount,
        petition.category,
        petition.region1depthName,
        petition.region2depthName,
        petition.supportCount))
        .from(likePetition)
        .join(likePetition.petition, petition)
        .where(
            petition.deleted.eq(false),
            likePetition.user.id.eq(viewerId)
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
    public List<ReadSimplePetitionsDto> findSupportPetitions(Pageable pageable, Long viewerId) {

        JPAQuery<ReadSimplePetitionsDto> query = queryFactory.select(new QReadSimplePetitionsDto(
        petition.id,
        petition.user.id,
        petition.title,
        petition.createdDate,
        petition.content,
        petition.photos,
        petition.likeCount,
        petition.dislikeCount,
        petition.progress,
        petition.goal,
        petition.replyCount,
        petition.category,
        petition.region1depthName,
        petition.region2depthName,
        petition.supportCount))
        .from(supportPetition)
        .join(supportPetition.petition, petition)
        .where(
            petition.deleted.eq(false),
            supportPetition.user.id.eq(viewerId)
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

    private BooleanExpression region1Filter(String region1Name) {
        if (StringUtils.hasText(region1Name)) {
            return petition.region1depthName.eq(region1Name) ;
        }
        return null;
    }

    private BooleanExpression region2Filter(String region2Name) {
        if (StringUtils.hasText(region2Name)) {
            return petition.region2depthName.eq(region2Name) ;
        }
        return null;
    }

    private BooleanExpression progressFilter(String progress) {
        if (StringUtils.hasText(progress)) {
            return petition.progress.eq(progress) ;
        }
        return null;
    }

    private BooleanExpression categoryFilter(String category) {
        if (StringUtils.hasText(category)) {
            return petition.category.eq(category) ;
        }
        return null;
    }
}
