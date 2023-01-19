package kalo.main.repository;

import java.time.LocalDateTime;
import java.util.List;

import kalo.main.domain.Ledger;

public interface LedgerRepositoryCustom {
    Long getSumUserLedger(Long userId);
    List<Ledger> isAttend(Long userId);
    List<LocalDateTime> getAttend(Long userId, int year, int month);
}
