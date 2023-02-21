package kalo.main.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String subtitle;
    String info;
    String thumbnail;
    String contentImage;
    Long vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="campaign_group_id")
    CampaignGroup campaignGroup;
}