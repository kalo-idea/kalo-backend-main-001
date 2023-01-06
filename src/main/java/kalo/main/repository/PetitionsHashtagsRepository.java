package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.PetitionsHashtags;

public interface PetitionsHashtagsRepository extends JpaRepository<PetitionsHashtags, Long> {
    
}
