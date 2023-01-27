package kalo.main.domain.dto.campaign;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CampaignsDto {
    MonthCampaignInfoDto info;
    List<CampaignInfoDto> campaigns;

    @Builder
    public CampaignsDto(MonthCampaignInfoDto info, List<CampaignInfoDto> campaigns) {
        this.info = info;
        this.campaigns = campaigns;
    }
}