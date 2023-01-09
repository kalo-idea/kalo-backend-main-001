package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    
}
