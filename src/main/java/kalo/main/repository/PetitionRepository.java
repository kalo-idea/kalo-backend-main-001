package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Petition;

public interface PetitionRepository extends JpaRepository<Petition, Long> {
    
}
