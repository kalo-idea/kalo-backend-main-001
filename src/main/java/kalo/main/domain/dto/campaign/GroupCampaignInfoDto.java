package kalo.main.domain.dto.campaign;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GroupCampaignInfoDto {
    Long donation;
    Long voteCount;
    LocalDateTime votingDateStart;
    LocalDateTime votingDateEnd;


    @Builder
    public GroupCampaignInfoDto(Long donation, Long voteCount, LocalDateTime votingDateStart, LocalDateTime votingDateEnd) {
        this.donation = donation;
        this.voteCount = voteCount;
        this.votingDateStart = votingDateStart;
        this.votingDateEnd = votingDateEnd;
    }
    
}
