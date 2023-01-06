package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.LikePosts;

public interface LikePostsRepository extends JpaRepository<LikePosts, Long> {
    Optional<LikePosts> findByPostsIdAndUsersId(Long postsId, Long userId);
}
