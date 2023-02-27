// package kalo.main.repository;

// import static kalo.main.domain.QTimeline.timeline;

// import com.querydsl.jpa.impl.JPAQueryFactory;

// import kalo.main.domain.dto.petition.QSimpleTimelineDto;
// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
// public class TimelineRepositoryImpl implements TimelineRepositoryCustom {
//     private final JPAQueryFactory queryFactory;

//     // @Override
//     // public List<SimpleTimelineDto> findListPosts(Pageable pageable, PostCondDto cond, Boolean recent) {

//     //     JPAQuery<SimpleTimelineDto> query = queryFactory.select(new QSimpleTimelineDto(
//     //         post.id,
//     //         post.user.id,
//     //         post.title,
//     //         post.createdDate,
//     //         post.content.substring(0, 100),
//     //         post.likeCount,
//     //         post.dislikeCount,
//     //         post.replyCount, 
//     //         post.topic, 
//     //         post.region1depthName,
//     //         post.region2depthName,
//     //         post.latitude,
//     //         post.longitude
//     //         )
//     //      )
//     //      .from(timeline)
//     //      .where(
//     //         userFilter(cond.getUserId()),
//     //         recentFilter(recent)
//     //      );

//     //     for (Sort.Order o : pageable.getSort()) {
//     //         PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
//     //         query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
//     //                 pathBuilder.get(o.getProperty())));
//     //     }
        
//     //     List<ReadSimplePostDto> result = query.fetch();

//     //     return result;    
//     // }
// }
