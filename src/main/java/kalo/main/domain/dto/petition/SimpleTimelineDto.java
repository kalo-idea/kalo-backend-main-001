package kalo.main.domain.dto.petition;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleTimelineDto {
    String title;
    String content;
    LocalDateTime atTime;

    @QueryProjection
    public SimpleTimelineDto(String title, String content, LocalDateTime atTime) {
        this.title = title;
        this.content = content;
        this.atTime = atTime;
    }

}
