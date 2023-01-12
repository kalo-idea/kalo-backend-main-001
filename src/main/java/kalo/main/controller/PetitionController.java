package kalo.main.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kalo.main.domain.dto.LikeDislikeResDto;
import kalo.main.domain.dto.ReplyDto;
import kalo.main.domain.dto.TargetIdUserIdDto;
import kalo.main.domain.dto.petition.CreatePetitionDto;
import kalo.main.domain.dto.petition.CreatePetitionReplyDto;
import kalo.main.domain.dto.petition.PetitionCondDto;
import kalo.main.domain.dto.petition.ReadPetitionDto;
import kalo.main.domain.dto.petition.ReadPetitionsDto;
import kalo.main.service.PetitionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PetitionController {
    
    private final PetitionService petitionService;

    // 청원 작성
    @PostMapping("/create_petition")
    public void createPetition(@RequestBody CreatePetitionDto createPetitionDto) {
        petitionService.createPetition(createPetitionDto);
    }

    // 청원 단건 조회
    @GetMapping("/public/get_petition")
    public ReadPetitionDto readPetition(TargetIdUserIdDto req) {
        return petitionService.readPetition(req.getTargetId(), req.getUserId());
    }

    // 청원 댓글 추가
    @PostMapping("/create_petition_reply")
    public void createPetitionReply(@RequestBody CreatePetitionReplyDto createPetitionReplyDto) {
        petitionService.createPetitionReply(createPetitionReplyDto);
    }

    // 청원 댓글 조회
    @GetMapping("/public/get_petition_replys")
    public List<ReplyDto> readComments(
        @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
        @RequestBody TargetIdUserIdDto req) {
        return petitionService.readPetitionsReply(req.getTargetId(), req.getUserId(), pageable);
    }

    // 청원 리스트 조회
    @GetMapping ("/public/get_petitions")
    public List<ReadPetitionsDto> readPetitions(
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
         PetitionCondDto cond
        ) {
            return petitionService.readPetitions(pageable, cond);
    }

    // 청원 게시글 좋아요
    @PostMapping("/like_petition")
    public LikeDislikeResDto likePetition(@RequestBody TargetIdUserIdDto req) {
        return petitionService.likePetition(req.getTargetId(), req.getUserId());
    }
 
    // 청원 게시글 싫어요
    @PostMapping("/dislike_petition")
    public LikeDislikeResDto dislikePetition(@RequestBody TargetIdUserIdDto req) {
        return petitionService.dislikePetition(req.getTargetId(), req.getUserId());
    }

    // 청원 댓글 좋아요
    @PostMapping("/like_petition_reply")
    public LikeDislikeResDto likePetitionReply(@RequestBody TargetIdUserIdDto req) {
        return petitionService.likePetitionReply(req.getTargetId(), req.getUserId());
    }
 
    // 청원 댓글 싫어요
    @PostMapping("/dislike_petition_reply")
    public LikeDislikeResDto dislikePetitionReply(@RequestBody TargetIdUserIdDto req) {
        return petitionService.dislikePetitionReply(req.getTargetId(), req.getUserId());
    }

    // 청원 참여하기
    @PostMapping("/support_petition")
    public ReadPetitionDto TargetId (TargetIdUserIdDto req) {
        return petitionService.supportingPetition(req.getTargetId(), req.getUserId());
    }

}
