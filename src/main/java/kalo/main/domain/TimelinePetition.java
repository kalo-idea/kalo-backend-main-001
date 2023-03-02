package kalo.main.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class TimelinePetition extends BaseEntity {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeline_id")
    Timeline timeline;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petition_id")
    Petition petition;

    @Builder
    public TimelinePetition(Timeline timeline, Petition petition) {
        this.timeline = timeline;
        this.petition = petition;
    }

}
