package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.CampaignUser;

public interface CampaignUserRepository extends JpaRepository<CampaignUser, Long>{

}
