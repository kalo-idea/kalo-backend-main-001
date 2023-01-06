package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.PostsHashtags;

public interface PostsHashtagsRepository extends JpaRepository<PostsHashtags, Long> {
    
}
