package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.DislikePetition;

public interface DislikePetitionRepository extends JpaRepository<DislikePetition, Long> {
    Optional<DislikePetition> findByPetitionIdAndUserIdAndDeleted(Long petitionId, Long userId, Boolean deleted);
}
