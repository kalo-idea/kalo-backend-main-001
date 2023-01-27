package kalo.main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
