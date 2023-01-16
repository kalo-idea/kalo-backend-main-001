package kalo.main.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SupportPetition extends BaseEntity {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petition_id")
    Petition petition;

    @Builder
    public SupportPetition(Long id, User user, Petition petition) {
        this.id = id;
        this.user = user;
        this.petition = petition;
    }

}