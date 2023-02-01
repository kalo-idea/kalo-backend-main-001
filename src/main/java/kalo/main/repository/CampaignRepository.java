package kalo.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Campaign;
import kalo.main.domain.CampaignGroup;

public interface CampaignRepository extends JpaRepository<Campaign, Long>{
    List<Campaign> findByCampaignGroup(CampaignGroup campaignGroup);
}
