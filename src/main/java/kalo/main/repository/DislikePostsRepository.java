package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.DislikePosts;

public interface DislikePostsRepository extends JpaRepository<DislikePosts, Long> {
    
}
