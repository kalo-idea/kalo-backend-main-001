package kalo.main.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kalo.main.domain.dto.petition.ReadPetitionsDto;
import kalo.main.domain.dto.users.OnlyUserIdDto;
import kalo.main.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get-like-petitions")
    public List<ReadPetitionsDto> readLikePetitions(
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, OnlyUserIdDto userId) {
            return userService.getLikePetitions(pageable, userId.getUserId());
    }

    @GetMapping("/get-support-petitions")
    public List<ReadPetitionsDto> readSupportPetitions(
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, OnlyUserIdDto userId) {
            return userService.getSupportPetitions(pageable, userId.getUserId());
    }
}
