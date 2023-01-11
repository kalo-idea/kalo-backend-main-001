package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.SupportPetition;

public interface SupportPetitionRepository extends JpaRepository<SupportPetition, Long> {
    Optional<SupportPetition> findByPetitionIdAndUserIdAndDeleted(Long petitionId, Long userId, Boolean deleted);
}
