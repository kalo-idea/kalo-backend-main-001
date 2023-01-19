package kalo.main.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Auth extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 32)
    String type;
    
    @Column(length = 32)
    String kakao;
    
    @Column(length = 32)
    String email;

    @Column(length = 32)
    String name;

    LocalDate birth;

    @Column(length = 8)
    String gender;

    String tel;

    String address;

    @Column(length = 32)
    String region1depthName;

    @Column(length = 32)
    String region2depthName;

    @Column(columnDefinition = "TINYINT", length = 1)
    Boolean promotionCheck;

    String fcmToken;

    LocalDateTime recentLogin;

    @Builder
    public Auth(Long id, String type, String kakao, String email, String name, LocalDate birth, String gender, String tel, String address, String region1depthName, String region2depthName, Boolean promotionCheck, String fcmToken, LocalDateTime recentLogin) {
        this.id = id;
        this.type = type;
        this.kakao = kakao;
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.tel = tel;
        this.address = address;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.promotionCheck = promotionCheck;
        this.fcmToken = fcmToken;
        this.recentLogin = recentLogin;
    }
    
}
