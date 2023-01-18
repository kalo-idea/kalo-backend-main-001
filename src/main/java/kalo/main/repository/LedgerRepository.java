package kalo.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kalo.main.domain.Ledger;

public interface LedgerRepository extends JpaRepository<Ledger, Long> {
    Optional<List<Ledger>> findByUserIdAndDeletedOrderByCreatedDateDesc(String userId, Boolean deleted);
    
    @Query(value = "select sum(l.amount) from ledger l where l.user_id = :userId", nativeQuery = true)
    Long getSumLedger(@Param("userId") Long userId);
}
