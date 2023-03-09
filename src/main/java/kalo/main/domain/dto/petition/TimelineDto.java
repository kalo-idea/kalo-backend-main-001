package kalo.main.domain.dto.petition;

import java.time.LocalDateTime;
import java.util.List;

import kalo.main.domain.Timeline;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimelineDto {
    String title;
    String content;
    String type;
    List<String> medium;
    LocalDateTime atTime;

    public TimelineDto(Timeline timeline, List<String> medium) {
        this.title = timeline.getTitle();
        this.content = timeline.getContent();
        this.medium = medium;
        this.type = timeline.getType();
        this.atTime = timeline.getAtTime();
    }

}
