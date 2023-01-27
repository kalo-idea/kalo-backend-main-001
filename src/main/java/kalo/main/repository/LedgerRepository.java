package kalo.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Ledger;

public interface LedgerRepository extends JpaRepository<Ledger, Long>, LedgerRepositoryCustom {
    // 회원용
    Optional<List<Ledger>> findByUserIdAndDeletedOrderByCreatedDateDesc(Long userId, Boolean deleted);
    
    // 관리자용
    Optional<List<Ledger>> findByUserIdOrderByCreatedDateDesc(Long userId);
}
