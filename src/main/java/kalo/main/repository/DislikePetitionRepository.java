package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.DislikePetition;

public interface DislikePetitionRepository extends JpaRepository<DislikePetition, Long> {
    
}
