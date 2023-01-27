package kalo.main.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Campaign;

public interface CampaignRepository extends JpaRepository<Campaign, Long>{
    List<Campaign> findByVotingDate(LocalDate votingDate);
}
