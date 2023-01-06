package kalo.main.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;

@Entity
@Getter
public class Auths extends BaseEntity {
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

    LocalDateTime birth;

    @Column(length = 8)
    String gender;

    String tel;

    String address;

    @Column(length = 16)
    String sido;

    @Column(length = 16)
    String sigugun;

    @Column(columnDefinition = "TINYINT", length = 1)
    Boolean promotionCheck;

    String fcmToken;

    LocalDateTime recentLogin;
}
