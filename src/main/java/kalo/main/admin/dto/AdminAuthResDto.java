package kalo.main.admin.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import kalo.main.domain.dto.SimpleWriterDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminAuthResDto {
    
    Long id;
    String type;
    String kakao;
    String email;
    String name;
    LocalDate birth;
    String gender;
    String tel;
    String address;
    String region1depthName;
    String region2depthName;
    Boolean promotionCheck;
    String fcmToken;
    LocalDateTime recentLogin;
    LocalDateTime createdDate;

    List<SimpleWriterDto> users;
}
