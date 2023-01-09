package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.PostReply;

public interface PostReplyRepository extends JpaRepository<PostReply, Long> {
    
}
