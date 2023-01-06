package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.PostsReplys;

public interface PostsReplysRepository extends JpaRepository<PostsReplys, Long> {
    
}
