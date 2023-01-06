package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.DislikePetitions;

public interface DislikePetitionsRepository extends JpaRepository<DislikePetitions, Long> {
    
}
