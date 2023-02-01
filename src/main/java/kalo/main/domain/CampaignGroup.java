package kalo.main.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignGroup extends BaseEntity {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    int year;
    int month;
    LocalDateTime supportingDateStart;
    LocalDateTime supportingDateEnd;
    LocalDateTime votingDateStart;
    LocalDateTime votingDateEnd;
    Long donation;
}
