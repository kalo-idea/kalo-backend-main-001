package kalo.main.domain.dto.petition;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PetitionCondDto {
    String search;
    String region1depthName;
    String region2depthName;
    String category;
    String progress;
    String step;

    public PetitionCondDto(String search, String region1depthName, String region2depthName, String category, String progress, String step) {
        this.search = search;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.category = category;
        this.progress = progress;
        this.step = step;
    }
}