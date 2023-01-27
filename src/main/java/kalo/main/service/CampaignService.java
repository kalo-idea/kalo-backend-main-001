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
import kalo.main.repository.UserRepository;
import kalo.main.controller.BasicException;
import kalo.main.domain.Campaign;
import kalo.main.domain.CampaignUser;
import kalo.main.domain.User;
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
    private final UserRepository userRepository;

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

    // 투표 상태 확인
    public String getVoteCampaignStatus(Long userId, int year, int month) {
        // impossible, possible, complete
        
        LocalDateTime beforeStartTime = LocalDate.of(year, month, 1).minusMonths(1).atStartOfDay();
        LocalDate beforeNextMonth = LocalDate.of(year, month, 1);
        LocalDateTime beforeEndTime = beforeNextMonth.minusDays(1).atTime(LocalTime.MAX);

        LocalDateTime startTime = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDate nextMonth = LocalDate.of(year, month, 1).plusMonths(1);
        LocalDateTime endTime = nextMonth.minusDays(1).atTime(LocalTime.MAX);

        // 투표권 보유 여부 확인
        // 투표권 보유
        if (supportPetitionRepository.findByUserIdAndDeletedAndCreatedDateBetween(userId, false, beforeStartTime, beforeEndTime).size() > 0) {
            
            // 투표 여부 확인
            if (campaignUserRepository.findByUserIdAndCreatedDateBetweenAndDeleted(userId, startTime, endTime, false).isPresent()) {
                return "complete";
            } else {
                return "possible";
            }

        // 투표권 미보유 (투표 불가)
        } else {
            return "impossible";
        }
    }

    // 투표하기
    public CampaignsDto voteCampaign(Long targetId, Long userId) {

        String voteStatus = getVoteCampaignStatus(userId, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
        
        if (voteStatus.equals("possible")) {
            
            LocalDateTime startTime = LocalDate.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 1).atStartOfDay();
            LocalDate nextMonth = LocalDate.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 1).plusMonths(1);
            LocalDateTime endTime = nextMonth.minusDays(1).atTime(LocalTime.MAX);

            // 삭제한 투표 기록 존재
            if (campaignUserRepository.findByUserIdAndCreatedDateBetweenAndDeleted(userId, startTime, endTime, true).isPresent()) {
                campaignUserRepository.findByUserIdAndCreatedDateBetweenAndDeleted(userId, startTime, endTime, true).get().revive();
                return getCampaigns(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
            } else {
                User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("없는 유저입니다."));
                Campaign campaign = campaignRepository.findById(targetId).orElseThrow(() -> new BasicException("없는 캠페인입니다."));
    
                CampaignUser campaignUser = CampaignUser.builder()
                .campaign(campaign)
                .user(user)
                .build();
    
                campaignUserRepository.save(campaignUser);
    
                return getCampaigns(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
            }
        } else if(voteStatus.equals("complete")) {
            throw new BasicException("이미 투표를 하셨습니다.");
        } else {
            throw new BasicException("투표권이 없습니다.");
        }
    }

    // 투표취소
    public CampaignsDto unvoteCampaign(Long targetId, Long userId) {
        String voteStatus = getVoteCampaignStatus(userId, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
        
        if (voteStatus.equals("complete")) {
            LocalDateTime startTime = LocalDate.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 1).atStartOfDay();
            LocalDate nextMonth = LocalDate.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 1).plusMonths(1);
            LocalDateTime endTime = nextMonth.minusDays(1).atTime(LocalTime.MAX);

            campaignUserRepository.findByUserIdAndCreatedDateBetweenAndDeleted(userId, startTime, endTime, false).orElseThrow(() -> new BasicException("삭제할 투표 기록이 없습니다.")).delete();

            return getCampaigns(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
        } else {
            throw new BasicException("투표 기록이 없습니다.");
        }
    }
}
