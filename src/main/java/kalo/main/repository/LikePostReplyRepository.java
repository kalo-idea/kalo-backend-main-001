package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.LikePostReply;

public interface LikePostReplyRepository extends JpaRepository<LikePostReply, Long> {
    
}
