package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.LikePetition;

public interface LikePetitionRepository extends JpaRepository<LikePetition, Long> {
    Optional<LikePetition> findByPetitionIdAndUserIdAndDeleted(Long petitionId, Long userId, Boolean deleted);
}
