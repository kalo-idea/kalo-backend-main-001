package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.LikePosts;

public interface LikePostsRepository extends JpaRepository<LikePosts, Long> {
    
}
