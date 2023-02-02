package kalo.main.repository;

import static kalo.main.domain.QPost.post;

import java.time.LocalDate;
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

import kalo.main.domain.dto.post.PostCondDto;
import kalo.main.domain.dto.post.QReadSimplePostDto;
import kalo.main.domain.dto.post.ReadSimplePostDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRespositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReadSimplePostDto> findListPosts(Pageable pageable, PostCondDto cond, Boolean recent) {

        JPAQuery<ReadSimplePostDto> query = queryFactory.select(new QReadSimplePostDto(
            post.id,
            post.user.id,
            post.title,
            post.createdDate,
            post.content,
            post.likeCount,
            post.dislikeCount,
            post.replyCount, 
            post.topic, 
            post.region1depthName,
            post.region2depthName)
         )
         .from(post)
         .where(
            post.deleted.eq(false),
            region1Filter(cond.getRegion1depthName()),
            region2Filter(cond.getRegion2depthName()),
            topicFilter(cond.getTopic()),
            userFilter(cond.getUserId()),
            recentFilter(recent)
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

    private BooleanExpression region1Filter(String region1Name) {
        if (StringUtils.hasText(region1Name)) {
            return post.region1depthName.eq(region1Name) ;
        }
        return null;
    }

    private BooleanExpression region2Filter(String region2Name) {
        if (StringUtils.hasText(region2Name)) {
            return post.region2depthName.eq(region2Name) ;
        }
        return null;
    }

    private BooleanExpression topicFilter(String topic) {
        if (StringUtils.hasText(topic)) {
            return post.topic.eq(topic);
        }
        return null;
    }

    private BooleanExpression userFilter(Long userId) {
        if (userId != null) {
            return post.user.id.eq(userId);
        }
        return null;
    }

    private BooleanExpression recentFilter(Boolean recent) {
        if (recent) {
            return post.createdDate.after(LocalDate.now().minusDays(29).atStartOfDay());
        }
        return null;
    }
}
