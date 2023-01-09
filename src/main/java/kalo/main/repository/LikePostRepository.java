package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.LikePost;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    Optional<LikePost> findByPostIdAndUserId(Long postId, Long userId);
}
