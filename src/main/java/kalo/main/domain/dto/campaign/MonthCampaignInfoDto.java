package kalo.main.domain.dto.campaign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MonthCampaignInfoDto {
    Long price;
    Long voteCount;


    @Builder
    public MonthCampaignInfoDto(Long price, Long voteCount) {
        this.price = price;
        this.voteCount = voteCount;
    }
}
