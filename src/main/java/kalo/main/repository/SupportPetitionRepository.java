package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.SupportPetition;

public interface SupportPetitionRepository extends JpaRepository<SupportPetition, Long> {
    
}
