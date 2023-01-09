package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Hashtags;

public interface HashtagsRepository extends JpaRepository<Hashtags, Long>, HashtagsRepositoryCustom {
    Optional<Hashtags> findByWord(String word);
}
