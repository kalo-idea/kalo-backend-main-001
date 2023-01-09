package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.DislikePetitionReply;

public interface DislikePetitionReplyRepository extends JpaRepository<DislikePetitionReply, Long> {
    
}
