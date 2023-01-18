package kalo.main.domain;

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
public class Petition extends BaseEntity {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title = "";

    @Column(columnDefinition = "TEXT")
    String content = "";

    Long supportCount = 0L;

    Long viewCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    User user;

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

    @Builder
    public Petition(Long id, String title, String content, Long supportCount, Long viewCount, User user, String progress, Long goal, Long replyCount, Long likeCount, Long dislikeCount, String addressName, String region1depthName, String region2depthName, String region3depthName, Double latitude, Double longitude, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.supportCount = supportCount;
        this.viewCount = viewCount;
        this.user = user;
        this.progress = progress;
        this.goal = goal;
        this.replyCount = replyCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.addressName = addressName;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.region3depthName = region3depthName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
    }

}
