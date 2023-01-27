package kalo.main.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.repository.CampaignUserRepository;
import kalo.main.repository.SupportPetitionRepository;
import kalo.main.domain.Campaign;
import kalo.main.domain.dto.campaign.CampaignInfoDto;
import kalo.main.domain.dto.campaign.CampaignsDto;
import kalo.main.domain.dto.campaign.MonthCampaignInfoDto;
import kalo.main.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final CampaignUserRepository campaignUserRepository;
    private final SupportPetitionRepository supportPetitionRepository;

    // 캠페인 목록
    public CampaignsDto getCampaigns(int year, int month) {

        // 적립 금액을 뽑아내는 방법 -> 해당 월 supportPetition에서 참여 갯수 * 150
        LocalDateTime startTime = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDate nextMonth = LocalDate.of(year, month, 1).plusMonths(1);
        LocalDateTime endTime = nextMonth.minusDays(1).atTime(LocalTime.MAX);
        
        Long supportCount = supportPetitionRepository.countByDeletedAndCreatedDateBetween(false, startTime, endTime);
        Long accruedAmount = supportCount * 150;

        LocalDate votingDate = LocalDate.of(year, month, 1);
        List<Campaign> campaigns = campaignRepository.findByVotingDate(votingDate);
        List<CampaignInfoDto> campaignInfos = new ArrayList<CampaignInfoDto>();
        Long voteCount = 0L;
        for (Campaign campaign : campaigns) {
            voteCount += campaign.getVote();
        }

        for (Campaign campaign : campaigns) {
            Double percent = 100 * campaign.getVote().doubleValue() / voteCount;
            campaignInfos.add(new CampaignInfoDto(campaign, percent));
        }
        MonthCampaignInfoDto info = MonthCampaignInfoDto.builder()
        .price(accruedAmount)
        .voteCount(voteCount)
        .build();

        CampaignsDto result = CampaignsDto.builder()
        .info(info)
        .campaigns(campaignInfos)
        .build();

        return result;
    }

    // 투표 가능여부 확인
    public String getVoteCampaignStatus(Long userId, int year, int month) {
        
        return null;
    }

    // 투표하기
    public CampaignsDto voteCampaign(Long targetId, Long userId) {

        return null;
    }

    // 투표취소
    public CampaignsDto unvoteCampaign(Long targetId, Long userId) {

        return null;
    }

}
