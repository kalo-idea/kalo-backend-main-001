package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.PetitionReply;

public interface PetitionReplyRepository extends JpaRepository<PetitionReply, Long> {
    
}
