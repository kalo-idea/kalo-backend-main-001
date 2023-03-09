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
    String type;
    LocalDateTime atTime;

    @Builder

    public Timeline(Long id, String title, String content, String type, LocalDateTime atTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.atTime = atTime;
    }
}
