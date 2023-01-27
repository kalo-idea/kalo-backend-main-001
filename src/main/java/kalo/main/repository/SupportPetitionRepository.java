package kalo.main.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.SupportPetition;

public interface SupportPetitionRepository extends JpaRepository<SupportPetition, Long> {
    Optional<SupportPetition> findByPetitionIdAndUserIdAndDeleted(Long petitionId, Long userId, Boolean deleted);
    Long countByUserId(Long userId);
    Long countByDeletedAndCreatedDateBetween(Boolean deleted, LocalDateTime start, LocalDateTime end);
    List<SupportPetition> findByUserIdAndDeletedAndCreatedDateBetween(Long userId, Boolean deleted, LocalDateTime start, LocalDateTime end);
}
