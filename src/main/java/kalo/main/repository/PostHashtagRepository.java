package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.PostHashtag;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
    
}
