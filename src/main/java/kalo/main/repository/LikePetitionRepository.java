package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.LikePetition;

public interface LikePetitionRepository extends JpaRepository<LikePetition, Long> {
    
}
