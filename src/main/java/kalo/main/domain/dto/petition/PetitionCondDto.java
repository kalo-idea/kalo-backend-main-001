package kalo.main.domain.dto.petition;

import lombok.Data;

@Data
public class PetitionCondDto {
    String sort;
    String region1depthName;
    String region2depthName;
    String category;
    String progress;

    public PetitionCondDto() {
    }

    public PetitionCondDto(String sort, String region1depthName, String region2depthName, String category, String progress) {
        this.sort = sort;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.category = category;
        this.progress = progress;
    }
}
