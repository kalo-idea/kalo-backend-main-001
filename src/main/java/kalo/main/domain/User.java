package kalo.main.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
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
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    LocalDateTime recentLogin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="auth_id")
    Auth auth;

    @Builder
    public User(Long id, String type, String nickname, String intro, String profileSrc, String publicInfos, LocalDateTime recentLogin, Auth auth) {
        this.id = id;
        this.type = type;
        this.nickname = nickname;
        this.intro = intro;
        this.profileSrc = profileSrc;
        this.publicInfos = publicInfos;
        this.recentLogin = recentLogin;
        this.auth = auth;
    }
}
