package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.DislikePost;

public interface DislikePostRepository extends JpaRepository<DislikePost, Long> {
    Optional<DislikePost> findByPostIdAndUserId(Long postsId, Long userId);
}
