package kalo.main.repository;

import static kalo.main.domain.QLikePetition.likePetition;
import static kalo.main.domain.QPetition.petition;
import static kalo.main.domain.QSupportPetition.supportPetition;
import static kalo.main.domain.QUser.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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

import kalo.main.domain.dto.petition.PetitionCondDto;
import kalo.main.domain.dto.petition.QReadSimplePetitionsDto;
import kalo.main.domain.dto.petition.QSupportPetitionUserListDto;
import kalo.main.domain.dto.petition.ReadSimplePetitionsDto;
import kalo.main.domain.dto.petition.SupportPetitionUserListDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PetitionRepositoryImpl implements PetitionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 청원 리스트 조회, 페이징, 조건 포함
    @Override
    public List<ReadSimplePetitionsDto> findListPetitions(Pageable pageable, PetitionCondDto cond, Boolean recent) {

        JPAQuery<ReadSimplePetitionsDto> query = queryFactory.select(new QReadSimplePetitionsDto(
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
            petition.supportCount))
        .from(petition)
        .where(
            petition.deleted.eq(false),
            searchFilter(cond.getSearch()),
            region1Filter(cond.getRegion1depthName()),
            region2Filter(cond.getRegion2depthName()),
            progressFilter(cond.getProgress()),
            stepFilter(cond.getStep()),
            categoryFilter(cond.getCategory()),
            recentFilter(recent)
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

    // 유저 좋아한 청원
    @Override
    public List<ReadSimplePetitionsDto> findLikePetitions(Pageable pageable, Long viewerId) {

        JPAQuery<ReadSimplePetitionsDto> query = queryFactory.select(new QReadSimplePetitionsDto(
        petition.id,
        petition.user.id,
        petition.title,
        petition.createdDate,
        petition.content,
        petition.likeCount,
        petition.dislikeCount,
        petition.progress,
        petition.step,
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
            likePetition.user.id.eq(viewerId),
            likePetition.deleted.eq(false)
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

    // 참여한 청원
    @Override
    public List<ReadSimplePetitionsDto> findSupportPetitions(Pageable pageable, Long viewerId) {

        JPAQuery<ReadSimplePetitionsDto> query = queryFactory.select(new QReadSimplePetitionsDto(
        petition.id,
        petition.user.id,
        petition.title,
        petition.createdDate,
        petition.content,
        petition.likeCount,
        petition.dislikeCount,
        petition.progress,
        petition.step,
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
            supportPetition.user.id.eq(viewerId),
            supportPetition.deleted.eq(false)
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

    // 청원에 참여한 사람 리스트
    @Override
    public List<SupportPetitionUserListDto> findSupportPetitionUserList(Pageable pageable, Long petitionId) {
        return queryFactory.select(new QSupportPetitionUserListDto(
            user.nickname,
            user.deleted,
            supportPetition.createdDate))
        .from(supportPetition)
        .join(supportPetition.user, user)
        .join(supportPetition.petition, petition)
        .where(petition.id.eq(petitionId))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(supportPetition.createdDate.desc())
        .fetch();
    }

    private BooleanExpression searchFilter(String search) {
        if (StringUtils.hasText(search)) {
            return petition.title.like("%" + search + "%").or(petition.content.like("%" + search + "%"));
        }
        return null;
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
            if (progress.equals("fail")) { // 실패
                return petition.progress.eq("recruit")
                    .and(petition.createdDate.lt(LocalDate.now().minusDays(29).atStartOfDay())
                      .and(petition.supportCount.lt(100)))
                .or(petition.progress.eq("fail"));
            }
            else if (progress.equals("recruit")) { // 모집 중
                return petition.progress.eq("recruit")
                .and(petition.createdDate.goe(LocalDate.now().minusDays(29).atStartOfDay()));
            }
            else if (progress.equals("ing")) { // 민원/건의, 언론 제보, 법률 검토
                return petition.progress.eq("recruit")
                    .and(petition.createdDate.lt(LocalDate.now().minusDays(29).atStartOfDay())
                        .and(petition.supportCount.goe(100)))
                .or(petition.progress.eq("ing"));
            }
            else if (progress.equals("complete")) { // 사회 참여 완료
                return petition.progress.eq("complete");
            }
            return null;
        }
        return null;
    }

    private BooleanExpression stepFilter(String step) {

        if (StringUtils.hasText(step)) {
            if (step.equals("suggest")) { // 실패
                return petition.progress.like("suggest");
            }
            if (step.equals("inform")) { // 실패
                return petition.progress.like("inform");
            }
            if (step.equals("legalReview")) { // 실패
                return petition.progress.like("legalReview");
            }
            return null;
        }
        return null;
    }

    private BooleanExpression categoryFilter(String category) {
        if (StringUtils.hasText(category)) {
            return petition.category.eq(category) ;
        }
        return null;
    }
    
    private BooleanExpression recentFilter(Boolean recent) {
        if (recent) {
            return petition.createdDate.after(LocalDate.now().minusDays(29).atStartOfDay());
        }
        return null;
    }
}
