package kalo.main.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.repository.CampaignUserRepository;
import kalo.main.repository.SupportPetitionRepository;
import kalo.main.repository.UserRepository;
import kalo.main.controller.BasicException;
import kalo.main.domain.Campaign;
import kalo.main.domain.CampaignGroup;
import kalo.main.domain.CampaignUser;
import kalo.main.domain.User;
import kalo.main.domain.dto.campaign.CampaignInfoDto;
import kalo.main.domain.dto.campaign.CampaignStatusDto;
import kalo.main.domain.dto.campaign.CampaignsDto;
import kalo.main.domain.dto.campaign.GroupCampaignInfoDto;
import kalo.main.repository.CampaignGroupRepository;
import kalo.main.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final CampaignGroupRepository campaignGroupRepository;
    private final CampaignUserRepository campaignUserRepository;
    private final SupportPetitionRepository supportPetitionRepository;
    private final UserRepository userRepository;

    public CampaignInfoDto getCampaign(Long id) {
        Campaign campaign = campaignRepository.findById(id).orElseThrow(() -> new BasicException("없는 캠페인입니다."));
        
        return new CampaignInfoDto(campaign, null);
    }

    // 캠페인 목록
    public CampaignsDto getCampaigns(int year, int month) {

        CampaignGroup campaignGroup = campaignGroupRepository.findByYearAndMonth(year, month).orElseThrow(() -> new BasicException("캠페인 그룹을 찾을 수 없습니다."));
        
        Long donation = campaignGroup.getDonation();

        List<Campaign> campaigns = campaignRepository.findByCampaignGroup(campaignGroup);
        if (!(campaigns.size() > 0)) {
            throw new BasicException("캠페인을 찾을 수 없습니다.");
        }

        List<CampaignInfoDto> campaignInfos = new ArrayList<CampaignInfoDto>();
        Long voteCount = 0L;
        for (Campaign campaign : campaigns) {
            voteCount += campaign.getVote();
        }

        for (Campaign campaign : campaigns) {
            if (voteCount == 0) {
                campaignInfos.add(new CampaignInfoDto(campaign, 0D));
            }
            else {
                Double percent = 100 * campaign.getVote().doubleValue() / voteCount;
                campaignInfos.add(new CampaignInfoDto(campaign, percent));
            }
        }
        GroupCampaignInfoDto info = GroupCampaignInfoDto.builder()
        .donation(donation)
        .voteCount(voteCount)
        .votingDateStart(campaignGroup.getVotingDateStart())
        .votingDateEnd(campaignGroup.getVotingDateEnd())
        .build();

        CampaignsDto result = CampaignsDto.builder()
        .info(info)
        .campaigns(campaignInfos)
        .build();

        return result;
    }

    // 투표 상태 확인
    public CampaignStatusDto getVoteCampaignStatus(Long userId, int year, int month) {
        // impossible, possible, complete
        CampaignGroup campaignGroup = campaignGroupRepository.findByYearAndMonth(year, month).orElseThrow(() -> new BasicException("캠페인 그룹을 찾을 수 없습니다."));
        CampaignStatusDto campaignStatus = new CampaignStatusDto();
        // 투표권 보유 여부 확인
        // 투표권 보유 
        if (supportPetitionRepository.findByUserIdAndDeletedAndCreatedDateBetween(userId, false, campaignGroup.getSupportingDateStart(), campaignGroup.getSupportingDateEnd()).size() > 0) {
            
            // 투표 여부 확인
            if (campaignUserRepository.findByUserIdAndCreatedDateBetweenAndDeleted(userId, campaignGroup.getVotingDateStart(), campaignGroup.getVotingDateEnd(), false).isPresent()) {
                campaignStatus.setStatus("complete");
                campaignStatus.setCampaignId(campaignUserRepository.findByUserIdAndCreatedDateBetweenAndDeleted(userId, campaignGroup.getVotingDateStart(), campaignGroup.getVotingDateEnd(), false).get().getCampaign().getId());
            } else {
                campaignStatus.setStatus("possible");
            }

        // 투표권 미보유 (투표 불가)
        } else {
            campaignStatus.setStatus("impossible");
        }
        return campaignStatus;
    }

    // 투표 기간 체크
    public Boolean getMyVoteCampaign(int year, int month) {
        CampaignGroup campaignGroup = campaignGroupRepository.findByYearAndMonth(year, month).orElseThrow(() -> new BasicException("캠페인 그룹을 찾을 수 없습니다."));
        
        if (LocalDateTime.now().isAfter(campaignGroup.getVotingDateStart()) && LocalDateTime.now().isBefore(campaignGroup.getVotingDateEnd())) {
            return true;
        } else {
            return false;
        }
    }

    // 투표하기
    public CampaignsDto voteCampaign(Long targetId, Long userId) {

        String voteStatus = getVoteCampaignStatus(userId, LocalDateTime.now().minusMonths(1).getYear(), LocalDateTime.now().minusMonths(1).getMonthValue()).getStatus();
        CampaignGroup campaignGroup = campaignGroupRepository.findByYearAndMonth(LocalDateTime.now().minusMonths(1).getYear(), LocalDateTime.now().minusMonths(1).getMonthValue()).orElseThrow(() -> new BasicException("캠페인 그룹을 찾을 수 없습니다."));
        
        if(campaignGroup.getVotingDateStart().isAfter(LocalDateTime.now()) || campaignGroup.getVotingDateEnd().isBefore(LocalDateTime.now())) {
            throw new BasicException("투표 기간이 아닙니다.");
        }

        if (voteStatus.equals("possible")) {
            // 삭제한 투표 기록 존재
            if (campaignUserRepository.findByUserIdAndCreatedDateBetweenAndDeleted(userId, campaignGroup.getVotingDateStart(), campaignGroup.getVotingDateEnd(), true).isPresent()) {
                campaignUserRepository.findByUserIdAndCreatedDateBetweenAndDeleted(userId, campaignGroup.getVotingDateStart(), campaignGroup.getVotingDateEnd(), true).get().revive();
                return getCampaigns(LocalDateTime.now().minusMonths(1).getYear(), LocalDateTime.now().minusMonths(1).getMonthValue());
            } else {
                User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("없는 유저입니다."));
                Campaign campaign = campaignRepository.findById(targetId).orElseThrow(() -> new BasicException("없는 캠페인입니다."));
    
                CampaignUser campaignUser = CampaignUser.builder()
                .campaign(campaign)
                .user(user)
                .build();
    
                campaignUserRepository.save(campaignUser);
                return getCampaigns(LocalDateTime.now().minusMonths(1).getYear(), LocalDateTime.now().minusMonths(1).getMonthValue());
            }
        } else if(voteStatus.equals("complete")) {
            throw new BasicException("이미 투표를 하셨습니다.");
        } else {
            throw new BasicException("투표권이 없습니다.");
        }
    }

    // 투표취소
    public CampaignsDto unvoteCampaign(Long targetId, Long userId) {
        String voteStatus = getVoteCampaignStatus(userId, LocalDateTime.now().minusMonths(1).getYear(), LocalDateTime.now().minusMonths(1).getMonthValue()).getStatus();
        CampaignGroup campaignGroup = campaignGroupRepository.findByYearAndMonth(LocalDateTime.now().minusMonths(1).getYear(), LocalDateTime.now().minusMonths(1).getMonthValue()).orElseThrow(() -> new BasicException("캠페인 그룹을 찾을 수 없습니다."));

        if (voteStatus.equals("complete")) {
            campaignUserRepository.findByUserIdAndCreatedDateBetweenAndDeleted(userId, campaignGroup.getVotingDateStart(), campaignGroup.getVotingDateEnd(), false).orElseThrow(() -> new BasicException("삭제할 투표 기록이 없습니다.")).delete();

            return getCampaigns(LocalDateTime.now().minusMonths(1).getYear(), LocalDateTime.now().minusMonths(1).getMonthValue());
        } else {
            throw new BasicException("투표 기록이 없습니다.");
        }
    }
}
