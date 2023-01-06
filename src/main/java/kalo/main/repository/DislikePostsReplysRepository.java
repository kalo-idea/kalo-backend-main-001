package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.DislikePostsReplys;

public interface DislikePostsReplysRepository extends JpaRepository<DislikePostsReplys, Long> {
    
}
