package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.MediaPost;

public interface MediaPostRepository extends JpaRepository<MediaPost, Long> {
    
}
