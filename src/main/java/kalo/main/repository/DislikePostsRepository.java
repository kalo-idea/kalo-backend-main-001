package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.DislikePosts;

public interface DislikePostsRepository extends JpaRepository<DislikePosts, Long> {
    Optional<DislikePosts> findByPostsIdAndUsersId(Long postsId, Long userId);
}
