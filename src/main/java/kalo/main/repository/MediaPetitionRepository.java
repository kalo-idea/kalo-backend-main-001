package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.MediaPetition;

public interface MediaPetitionRepository extends JpaRepository<MediaPetition, Long> {
    
}
