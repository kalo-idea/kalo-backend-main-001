package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.DislikePostReply;

public interface DislikePostReplyRepository extends JpaRepository<DislikePostReply, Long> {
    Optional<DislikePostReply> findByPostReplyIdAndUserIdAndDeleted(Long postReplyId, Long userId, Boolean deleted);
}
