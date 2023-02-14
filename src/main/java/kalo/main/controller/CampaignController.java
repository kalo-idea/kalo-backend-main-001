package kalo.main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kalo.main.domain.dto.TargetIdUserIdDto;
import kalo.main.domain.dto.campaign.CampaignStatusDto;
import kalo.main.domain.dto.campaign.CampaignsDto;
import kalo.main.service.CampaignService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService campaignService;

    // 캠페인 리스트 조회
    @GetMapping("/public/get-campaigns")
    public CampaignsDto get(int year, int month) {
        return campaignService.getCampaigns(year, month);
    }

    // 캠페인 투표
    @PostMapping("/vote-campaign")
    public CampaignsDto voteCampaign(@RequestBody TargetIdUserIdDto req) {
        return campaignService.voteCampaign(req.getTargetId(), req.getUserId());
    }

    // 캠페인 투표 취소
    @PostMapping("/unvote-campaign")
    public CampaignsDto unvoteCampaign(@RequestBody TargetIdUserIdDto req) {
        return campaignService.unvoteCampaign(req.getTargetId(), req.getUserId());
    }

    // 캠페인 상태
    @GetMapping("/get-vote-campaign-status")
    public CampaignStatusDto getVoteCampaignStatus(Long id, int year, int month) {
        return campaignService.getVoteCampaignStatus(id, year, month);
    }

}
