package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.LikePostReply;

public interface LikePostReplyRepository extends JpaRepository<LikePostReply, Long> {
    Optional<LikePostReply> findByPostReplyIdAndUserIdAndDeleted(Long postReplyId, Long userId, Boolean deleted);
}
