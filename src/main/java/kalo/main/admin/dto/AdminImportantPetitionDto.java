package kalo.main.admin.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminImportantPetitionDto {
    Long id;
    String title;
    String content;
    String imageSrc;
    LocalDateTime importantEndDate;
}
