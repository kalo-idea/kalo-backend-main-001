package kalo.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Ledgers;

public interface LedgersRepository extends JpaRepository<Ledgers, Long> {
    Optional<List<Ledgers>> findByUsersIdOrderByCreatedAtDesc(String userId);
}
