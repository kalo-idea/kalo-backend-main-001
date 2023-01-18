package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Media;

public interface MediaRepository extends JpaRepository<Media, Long>, MediaRepositoryCustom {
    Optional<Media> findByFileNameAndDeleted(String fileName, Boolean deleted);
}
