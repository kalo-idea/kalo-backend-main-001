package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.CampaignGroup;

public interface CampaignGroupRepository extends JpaRepository<CampaignGroup, Long> {
    Optional<CampaignGroup> findByYearAndMonth(int year, int month);
}
