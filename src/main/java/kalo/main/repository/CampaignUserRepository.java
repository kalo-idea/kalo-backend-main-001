package kalo.main.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.CampaignUser;

public interface CampaignUserRepository extends JpaRepository<CampaignUser, Long>{
    Optional<CampaignUser> findByUserIdAndCreatedDateBetweenAndDeleted(Long userId, LocalDateTime beforeStartTime, LocalDateTime beforeEndTime, Boolean deleted);
    
}