package kalo.main.domain.dto.campaign;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CampaignStatusDto {
    String status;
    Long campaignId;
}
