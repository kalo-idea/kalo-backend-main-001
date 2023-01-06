package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.PetitionsReplys;

public interface PetitionsReplysRepository extends JpaRepository<PetitionsReplys, Long> {
    
}
