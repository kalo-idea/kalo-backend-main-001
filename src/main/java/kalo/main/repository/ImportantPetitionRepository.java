package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.ImportantPetition;

public interface ImportantPetitionRepository extends JpaRepository<ImportantPetition, Long>{
    
}
