package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.LikePostsReplys;

public interface LikePostsReplysRepository extends JpaRepository<LikePostsReplys, Long> {
    
}
