package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.LikePetitions;

public interface LikePetitionsRepository extends JpaRepository<LikePetitions, Long> {
    
}
