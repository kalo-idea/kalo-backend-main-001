package kalo.main.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kalo.main.domain.dto.petition.ReadPetitionsDto;
import kalo.main.domain.dto.users.OnlyUserIdDto;
import kalo.main.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get_like_petition")
    public List<ReadPetitionsDto> readPetitions(
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
        @RequestBody OnlyUserIdDto userId
        ) {
            return userService.getLikePetitions(pageable, userId.getUserId());
    }
}
