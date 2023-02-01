package kalo.main.domain.dto.campaign;

import kalo.main.domain.Campaign;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CampaignInfoDto {
    Long id;
    String title;
    String subTitle;
    String info;
    String thumbnail;
    String contentImage;
    Long vote;
    Double percent;

    @Builder
    public CampaignInfoDto(Campaign campaign, Double percent) {
        this.id = campaign.getId();
        this.title = campaign.getTitle();
        this.subTitle = campaign.getSubTitle();
        this.info = campaign.getInfo();
        this.thumbnail = campaign.getThumbnail();
        this.contentImage = campaign.getContentImage();
        this.vote = campaign.getVote();
        this.percent = percent;
    }
}
