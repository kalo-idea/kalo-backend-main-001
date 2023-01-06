package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.SupportPetitions;

public interface SupportPetitionsRepository extends JpaRepository<SupportPetitions, Long> {
    
}
