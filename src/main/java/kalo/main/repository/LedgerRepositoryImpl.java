package kalo.main.repository;

import static kalo.main.domain.QLedger.ledger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kalo.main.domain.Ledger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LedgerRepositoryImpl implements LedgerRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    // 유저 포인트 합계
    @Override
    public Long getSumUserLedger(Long userId) {
        return queryFactory.select(ledger.amount.sum())
        .from(ledger)
        .where(ledger.user.id.eq(userId).and(ledger.deleted.eq(false)))
        .fetchOne();
    }

    // 유저 금일 출석 체크 여부
    @Override
    public List<Ledger> isAttend(Long userId) {
        return queryFactory.select(ledger)
        .from(ledger)
        .where(ledger.user.id.eq(userId)
            .and(ledger.createdDate.after(LocalDate.now().atStartOfDay()))
            .and(ledger.type.eq("attendance"))
            .and(ledger.deleted.eq(false))
            )
        .fetch();
    }

    // 이번달 출석일자
    public List<LocalDateTime> getAttend(Long userId, int year, int month) {
        return queryFactory.select(ledger.createdDate)
        .from(ledger)
        .where(ledger.user.id.eq(userId)
            .and(ledger.createdDate.year().eq(year))
            .and(ledger.createdDate.month().eq(month))
            .and(ledger.type.eq("attendance"))
            .and(ledger.deleted.eq(false))
        )
        .fetch();
    }
}
