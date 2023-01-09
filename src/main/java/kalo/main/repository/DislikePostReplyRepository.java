package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.DislikePostReply;

public interface DislikePostReplyRepository extends JpaRepository<DislikePostReply, Long> {
    
}
