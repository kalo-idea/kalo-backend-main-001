package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Petitions;

public interface PetitionsRepository extends JpaRepository<Petitions, Long> {
    
}
