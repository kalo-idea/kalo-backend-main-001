package kalo.main.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kalo.main.domain.dto.LikeDislikeResDto;
import kalo.main.domain.dto.ReplyDto;
import kalo.main.domain.dto.TargetIdUserIdDto;
import kalo.main.domain.dto.petition.CreatePetitionDto;
import kalo.main.domain.dto.petition.CreatePetitionReplyDto;
import kalo.main.domain.dto.petition.ImportantPetitionResDto;
import kalo.main.domain.dto.petition.PetitionCondDto;
import kalo.main.domain.dto.petition.ReadPetitionDto;
import kalo.main.domain.dto.petition.ReadPetitionsDto;
import kalo.main.domain.dto.petition.SupportPetitionUserListDto;
import kalo.main.service.PetitionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PetitionController {
    
    private final PetitionService petitionService;

    // 청원 작성
    @PostMapping("/create-petition")
    public Long createPetition(@Valid @RequestBody CreatePetitionDto createPetitionDto) {
        return petitionService.createPetition(createPetitionDto);
    }

    // 청원 단건 조회
    @GetMapping("/public/get-petition")
    public ReadPetitionDto readPetition(TargetIdUserIdDto req) {
        return petitionService.readPetition(req.getTargetId(), req.getUserId());
    }

    // 청원 댓글 추가
    @PostMapping("/create-petition-reply")
    public void createPetitionReply(@Valid @RequestBody CreatePetitionReplyDto createPetitionReplyDto) {
        petitionService.createPetitionReply(createPetitionReplyDto);
    }

    // 청원 댓글 조회
    @GetMapping("/public/get-petition-replys")
    public List<ReplyDto> readComments(
        @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
        TargetIdUserIdDto req) {
        return petitionService.readPetitionsReply(req.getTargetId(), req.getUserId(), pageable);
    }

    // 청원 리스트 조회
    @GetMapping ("/public/get-petitions")
    public List<ReadPetitionsDto> readPetitions(
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
         PetitionCondDto cond,
         @RequestParam(defaultValue = "false") Boolean recent
        ) {
            return petitionService.readPetitions(pageable, cond, recent);
    }

    // 청원 게시글 좋아요
    @PostMapping("/like-petition")
    public LikeDislikeResDto likePetition(@Valid @RequestBody TargetIdUserIdDto req) {
        return petitionService.likePetition(req);
    }
 
    // 청원 게시글 싫어요
    @PostMapping("/dislike-petition")
    public LikeDislikeResDto dislikePetition(@Valid @RequestBody TargetIdUserIdDto req) {
        return petitionService.dislikePetition(req);
    }

    // 청원 댓글 좋아요
    @PostMapping("/like-petition-reply")
    public LikeDislikeResDto likePetitionReply(@Valid @RequestBody TargetIdUserIdDto req) {
        return petitionService.likePetitionReply(req);
    }
 
    // 청원 댓글 싫어요
    @PostMapping("/dislike-petition-reply") 
    public LikeDislikeResDto dislikePetitionReply(@Valid @RequestBody TargetIdUserIdDto req) {
        return petitionService.dislikePetitionReply(req);
    }

    // 청원 참여하기
    @PostMapping("/support-petition")
    public ReadPetitionDto TargetId (@Valid @RequestBody TargetIdUserIdDto req) {
        return petitionService.supportingPetition(req.getTargetId(), req.getUserId());
    }

    // 청원 참여 리스트
    @GetMapping("/public/get-support-users")
    public List<SupportPetitionUserListDto> getSupportPetitionList(
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
        Long id) {
        return petitionService.getSupportPetitionList(pageable, id);
    }

    // 해쉬태그 검색
    @GetMapping("/get-petition-hashtag")
    public List<ReadPetitionsDto> getHashtags(
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
        String hashtag
    ) {
        return petitionService.getPetitionsByHashtag(pageable, hashtag);
    }

    // 중요 청원 검색
    @GetMapping("/get-important-petiton")
    public List<ImportantPetitionResDto> getImportantPetiton(
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return petitionService.getImportantPetitons(pageable);
    }
}
