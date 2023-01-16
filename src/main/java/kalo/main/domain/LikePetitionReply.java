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
<<<<<<<< HEAD:src/main/java/kalo/main/domain/LikePetitionReply.java
public class LikePetitionReply extends BaseEntity {
========
public class SupportPetition extends BaseEntity {
>>>>>>>> origin/develop:src/main/java/kalo/main/domain/SupportPetition.java
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

<<<<<<<< HEAD:src/main/java/kalo/main/domain/LikePetitionReply.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petition_reply_id")
    PetitionReply petitionReply;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Builder
    public LikePetitionReply(PetitionReply petitionReply, User user) {
        this.petitionReply = petitionReply;
        this.user = user;
    }
========
    String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petition_id")
    Petition petition;

    @Builder
    public SupportPetition(Long id, String nickname, User user, Petition petition) {
        this.id = id;
        this.nickname = nickname;
        this.user = user;
        this.petition = petition;
    }

>>>>>>>> origin/develop:src/main/java/kalo/main/domain/SupportPetition.java
}
