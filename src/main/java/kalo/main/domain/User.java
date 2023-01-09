package kalo.main.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;

@Entity
@Getter
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 32)
    String type;
    
    @Column(length = 32)
    String nickname;

    String intro;
    String profileSrc;
    String publicInfos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="auth_id")
    Auth auth;
}
