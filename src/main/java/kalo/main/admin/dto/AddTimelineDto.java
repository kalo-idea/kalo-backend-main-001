package kalo.main.admin.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddTimelineDto {
    Long id;
    LocalDateTime atTime;
    
    String title;
    String content;
    String type;
    List<String> medium;
}
