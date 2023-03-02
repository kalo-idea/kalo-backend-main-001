package kalo.main.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Timeline extends BaseEntity {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    String title;
    String content;
    LocalDateTime atTime;

    @Builder
    public Timeline(String title, String content, LocalDateTime atTime) {
        this.title = title;
        this.content = content;
        this.atTime = atTime;
    }

}
