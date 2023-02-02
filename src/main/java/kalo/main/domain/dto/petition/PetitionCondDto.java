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
    Long userId;

    public PetitionCondDto(String search, String region1depthName, String region2depthName, String category, String progress, String step, Long userId) {
        this.search = search;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.category = category;
        this.progress = progress;
        this.step = step;
        this.userId = userId;
    }
}