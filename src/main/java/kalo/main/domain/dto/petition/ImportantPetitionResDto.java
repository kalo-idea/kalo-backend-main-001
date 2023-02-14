package kalo.main.domain.dto.petition;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;

@Data
public class ImportantPetitionResDto {
    
    String priorityTitle;
    String priotityContent;
    String priorityImageSrc;

    String title;
    String content;
    LocalDateTime supportingDateEnd;

    @Builder
    @QueryProjection
    public ImportantPetitionResDto(String priorityTitle, String priotityContent, String priorityImageSrc, String title, String content, LocalDateTime supportingDateEnd) {
        this.priorityTitle = priorityTitle;
        this.priotityContent = priotityContent;
        this.priorityImageSrc = priorityImageSrc;
        this.title = title;
        this.content = content;
        this.supportingDateEnd = supportingDateEnd;
    }    

}
