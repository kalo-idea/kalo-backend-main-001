package kalo.main.domain.dto.campaign;

import java.time.LocalDate;

import kalo.main.domain.Campaign;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CampaignInfoDto {
    LocalDate votingDate;
    String title;
    String info;
    String thumbnail;
    String contentImage;
    Long vote;
    Double percent;

    @Builder
    public CampaignInfoDto(Campaign campaign, Double percent) {
        this.votingDate = campaign.getVotingDate();
        this.title = campaign.getTitle();
        this.info = campaign.getInfo();
        this.thumbnail = campaign.getThumbnail();
        this.contentImage = campaign.getContentImage();
        this.vote = campaign.getVote();
        this.percent = percent;
    }
}
