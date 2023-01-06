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
public class Petitions {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title = "";

    @Column(columnDefinition = "TEXT")
    String content = "";

    String photos = "";

    Long supportCount = 0L;

    Long viewCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="users_id")
    Users users;

    String progress = "";

    Long goal = 0L;

    Long replyCount = 0L;

    Long likeCount = 0L;

    Long dislikeCount = 0L;

    String addressName = "";

    @Column(length = 32)
    String region1depthName = "";

    @Column(length = 32)
    String region2depthName = "";

    @Column(length = 32)
    String region3depthName = "";

    Double latitude = 0d;

    Double longitude = 0d;

    String category;
}
