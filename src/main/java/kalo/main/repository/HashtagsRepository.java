package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Hashtags;

public interface HashtagsRepository extends JpaRepository<Hashtags, Long> {
    
}
