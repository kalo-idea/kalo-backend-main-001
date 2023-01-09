package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.PetitionHashtag;

public interface PetitionHashtagRepository extends JpaRepository<PetitionHashtag, Long> {
    
}
