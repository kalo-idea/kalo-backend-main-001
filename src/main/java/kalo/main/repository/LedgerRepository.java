package kalo.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Ledger;

public interface LedgerRepository extends JpaRepository<Ledger, Long> {
    Optional<List<Ledger>> findByUserIdAndDeletedOrderByCreatedDateDesc(String userId, Boolean deleted);
}
