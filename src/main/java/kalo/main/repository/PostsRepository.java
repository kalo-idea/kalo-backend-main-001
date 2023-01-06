package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Posts;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    
}
