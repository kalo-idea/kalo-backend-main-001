package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.LikePetitionReply;

public interface LikePetitionReplyRepository extends JpaRepository<LikePetitionReply, Long> {
    
}
