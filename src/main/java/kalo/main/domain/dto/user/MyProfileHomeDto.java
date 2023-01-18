package kalo.main.domain.dto.user;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyProfileHomeDto {
    Long point;
    Long supportPetition;
    Long likePetition;

    @QueryProjection
    public MyProfileHomeDto(Long point, Long supportPetition, Long likePetition) {
        this.point = point;
        this.supportPetition = supportPetition;
        this.likePetition = likePetition;
    }

}
