package kalo.main.domain.dto.user;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyProfileHomeDto {
    Long point;
    Long supportPetitionCount;
    Long likePetitionCount;

    @QueryProjection
    public MyProfileHomeDto(Long point, Long supportPetitionCount, Long likePetitionCount) {
        this.point = point;
        this.supportPetitionCount = supportPetitionCount;
        this.likePetitionCount = likePetitionCount;
    }

}
