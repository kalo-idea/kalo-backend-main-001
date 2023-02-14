package kalo.main.domain.dto.petition;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleImportantPetitionDto {
    String imageSrc;
    String content;


    public SimpleImportantPetitionDto(String imageSrc, String content) {
        this.imageSrc = imageSrc;
        this.content = content;
    }
}
