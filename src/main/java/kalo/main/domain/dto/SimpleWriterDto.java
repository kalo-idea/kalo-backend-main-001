package kalo.main.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleWriterDto {
    Long userId;
    String nickname;
    String profileSrc;
}
