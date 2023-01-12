package kalo.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.domain.DislikePetition;
import kalo.main.domain.DislikePetitionReply;
import kalo.main.domain.Hashtag;
import kalo.main.domain.LikePetition;
import kalo.main.domain.LikePetitionReply;
import kalo.main.domain.Petition;
import kalo.main.domain.PetitionHashtag;
import kalo.main.domain.PetitionReply;
import kalo.main.domain.SupportPetition;
import kalo.main.domain.User;
import kalo.main.domain.dto.LikeDislikeResDto;
import kalo.main.domain.dto.ReplyDto;
import kalo.main.domain.dto.petition.CreatePetitionDto;
import kalo.main.domain.dto.petition.CreatePetitionReplyDto;
import kalo.main.domain.dto.petition.PetitionCondDto;
import kalo.main.domain.dto.petition.ReadPetitionDto;
import kalo.main.domain.dto.petition.ReadPetitionsDto;
import kalo.main.domain.dto.petition.ReadSimplePetitionsDto;
import kalo.main.repository.DislikePetitionReplyRepository;
import kalo.main.repository.DislikePetitionRepository;
import kalo.main.repository.HashtagRepository;
import kalo.main.repository.LikePetitionReplyRepository;
import kalo.main.repository.LikePetitionRepository;
import kalo.main.repository.PetitionHashtagRepository;
import kalo.main.repository.PetitionReplyRepository;
import kalo.main.repository.PetitionRepository;
import kalo.main.repository.SupportPetitionRepository;
import kalo.main.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PetitionService {

    private final PetitionRepository petitionRepository;
    private final UsersRepository usersRepository;
    private final PetitionHashtagRepository petitionHashtagRepository;
    private final HashtagRepository hashtagRepository;
    private final LikePetitionRepository likePetitionRepository;
    private final DislikePetitionRepository dislikePetitionRepository;
    private final PetitionReplyRepository petitionReplyRepository;
    private final LikePetitionReplyRepository likePetitionReplyRepository;
    private final DislikePetitionReplyRepository dislikePetitionReplyRepository;
    private final SupportPetitionRepository supportPetitionRepository;

    // 청원 생성
    public Long createPetition(CreatePetitionDto createPetitionDto) {
        
        Petition petition = Petition.builder()
        .title(createPetitionDto.getTitle())
        .content(createPetitionDto.getContent())
        .photos(createPetitionDto.getPhotos())
        .supportCount(0L)
        .viewCount(0L)
        .user(usersRepository.findById(createPetitionDto.getUserId()).get())
        .progress("모집")
        .goal(createPetitionDto.getGoal())
        .replyCount(0L)
        .likeCount(0L)
        .dislikeCount(0L)
        .addressName(createPetitionDto.getAddressName())
        .region1depthName(createPetitionDto.getRegion1depthName())
        .region2depthName(createPetitionDto.getRegion2depthName())
        .region3depthName(createPetitionDto.getRegion3depthName())
        .latitude(createPetitionDto.getLatitude())
        .longitude(createPetitionDto.getLongitude())
        .category(createPetitionDto.getCategory())
        .build();

        Petition resultPetition = petitionRepository.save(petition);
        
        List<String> hashtags = createPetitionDto.getHashtags();
        for (String hashtag : hashtags) {
            try {
                // hashtag find
                PetitionHashtag postsHashtag = PetitionHashtag.builder()
                    .petition(resultPetition)
                    .hashtag(hashtagRepository.findByWordAndDeleted(hashtag, false).orElseThrow())
                    .build();
                petitionHashtagRepository.save(postsHashtag);
            } catch(NoSuchElementException e) {
                // hashtag make
                Hashtag makeHashtag = new Hashtag(hashtag);
                hashtagRepository.save(makeHashtag);
                PetitionHashtag postsHashtag = PetitionHashtag.builder()
                    .petition(resultPetition)
                    .hashtag(makeHashtag)
                    .build();
                petitionHashtagRepository.save(postsHashtag);
            }
        }

        return resultPetition.getId();
    }

    // 청원 단건 조회
    public ReadPetitionDto readPetition(Long petitionId, Long viewerId) {

        Boolean isLike = false;
        Boolean isDislike = false;
        Boolean isSupport = false;
        if (viewerId != null) { // 로그인의 경우 좋아요, 싫어요 여부 확인
            isLike = likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, viewerId, false).isPresent();
            isDislike = dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, viewerId, false).isPresent();
            isSupport = supportPetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, viewerId, false).isPresent();
        }
        Petition petition = petitionRepository.findById(petitionId).get();
        
        // 삭제된 게시글이면 널 반환
        if (petition.getDeleted()) {
            return null;
        }

        User writer = usersRepository.findById(petition.getUser().getId()).get();
        List<Hashtag> hashs = hashtagRepository.findPetitionHashtags(petitionId);
        List<String> hashtags = new ArrayList();
        System.out.println("writer : " + writer.getNickname());
        System.out.println("writer de: " + writer.getDeleted());

        for (Hashtag hash : hashs) {
            hashtags.add(hash.getWord());
        }

        petition.setViewCount(petition.getViewCount() + 1);

        // 탈퇴회원 -> 유저정보 제외하고 반환
        if (writer.getDeleted()) {
            return ReadPetitionDto.builder()
            .title(petition.getTitle())
            .createdDate(petition.getCreatedDate())
            .content(petition.getContent())
            .hashtags(hashtags)
            .photos(petition.getPhotos())
            .likeCount(petition.getLikeCount())
            .isLike(isLike)
            .dislikeCount(petition.getDislikeCount())
            .isDislike(isDislike)
            .progress(petition.getProgress())
            .goal(petition.getGoal())
            .replyCount(petition.getReplyCount())
            .category(petition.getCategory())
            .region1depthName(petition.getRegion1depthName())
            .region2depthName(petition.getRegion2depthName())
            .supportCount(petition.getSupportCount())
            .isSupport(isSupport)
            .build();
        }
        // 정상회원
        return ReadPetitionDto.builder()
        .userId(writer.getId())
        .nickname(writer.getNickname())
        .profileSrc(writer.getProfileSrc())
        .title(petition.getTitle())
        .createdDate(petition.getCreatedDate())
        .content(petition.getContent())
        .hashtags(hashtags)
        .photos(petition.getPhotos())
        .likeCount(petition.getLikeCount())
        .isLike(isLike)
        .dislikeCount(petition.getDislikeCount())
        .isDislike(isDislike)
        .progress(petition.getProgress())
        .goal(petition.getGoal())
        .replyCount(petition.getReplyCount())
        .category(petition.getCategory())
        .region1depthName(petition.getRegion1depthName())
        .region2depthName(petition.getRegion2depthName())
        .supportCount(petition.getSupportCount())
        .isSupport(isSupport)
        .build();
    }

    // 청원 댓글 추가
    public Long createPetitionReply(CreatePetitionReplyDto createPetitionReplyDto) {
        PetitionReply petitionReply = PetitionReply.builder()
        .user(usersRepository.findById(createPetitionReplyDto.getUserId()).get())
        .content(createPetitionReplyDto.getContent())
        .petition(petitionRepository.findById(createPetitionReplyDto.getPetitionId()).get())
        .likeCount(0L)
        .dislikeCount(0L)
        .build();

        PetitionReply result = petitionReplyRepository.save(petitionReply);

        return result.getId();
    }


    // 청원 댓글 조회
    public List<ReplyDto> readPetitionsReply(Long petitionId, Long viewerId, Pageable pageable) {

        List<PetitionReply> replys = petitionReplyRepository.findByPetitionIdAndDeleted(petitionId, pageable, false);

        List<ReplyDto> result = new ArrayList();

        for (PetitionReply reply : replys) {
            Boolean isLike = false;
            Boolean isDislike = false;
            if (viewerId != null) {
                likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(reply.getId(), viewerId, false).isPresent();
                dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(reply.getId(), viewerId, false).isPresent();
            }
            // 댓글 작성자 탈퇴의 경우
            if (reply.getUser().getDeleted()) {
                ReplyDto commentDto = new ReplyDto().builder()
                .commentId(reply.getId())
                .isLike(isLike)
                .likeCount(reply.getLikeCount())
                .isDislike(isDislike)
                .dislikeCount(reply.getDislikeCount())
                .content(reply.getContent())
                .build();
    
                result.add(commentDto);
            }
            if (!reply.getUser().getDeleted()) {
                ReplyDto commentDto = new ReplyDto().builder()
                .commentId(reply.getId())
                .userId(reply.getUser().getId())
                .nickname(reply.getUser().getNickname())
                .profileSrc(reply.getUser().getProfileSrc())
                .isLike(isLike)
                .likeCount(reply.getLikeCount())
                .isDislike(isDislike)
                .dislikeCount(reply.getDislikeCount())
                .content(reply.getContent())
                .build();
    
                result.add(commentDto);
            }
        }
        return result;
    }

    // 청원 리스트 조회
    public List<ReadPetitionsDto> readPetitions(Pageable pageable, PetitionCondDto cond) {

        List<ReadSimplePetitionsDto> simplePetitions = petitionRepository.findListPetitions(pageable, cond);

        List<ReadPetitionsDto> result = new ArrayList<>();
        for (ReadSimplePetitionsDto simplePetition : simplePetitions) {
            User writer = usersRepository.findById(simplePetition.getWriterId()).get();
            List<String> words = new ArrayList();
            List<Hashtag> hashtags = hashtagRepository.findPetitionHashtags(simplePetition.getPetitionId());
            for (Hashtag hashtag : hashtags) {
                words.add(hashtag.getWord());
            }
            if (writer.getDeleted()) {
                result.add(new ReadPetitionsDto(simplePetition, null, null, null, words));
            }
            else {
                result.add(new ReadPetitionsDto(simplePetition, writer.getId(), writer.getNickname(), writer.getProfileSrc(), words));
            }
        }

        return result;
    }

    // 게시글 좋아요, 좋아요 취소
    // 좋아요 클릭
    public LikeDislikeResDto likePetition(Long petitionId, Long userId) {
        Petition petition = petitionRepository.findById(petitionId).get();
        User user = usersRepository.findById(userId).get();
        
        // 이미 좋아요를 누른 상태 : 좋아요를 취소 -> 좋아요 -1
        if (likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent()) {
            likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).get().delete();
            petition.setLikeCount(petition.getLikeCount() - 1);
        }
        // 싫어요가 눌려있던 상태 : 좋아요 추가, 싫어요 취소 -> 좋아요 +1, 싫어요 -1
        else if (dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent()) {
            dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).get().delete();
            LikePetition likePetition = LikePetition.builder().petition(petition).user(user).build();
            likePetitionRepository.save(likePetition);

            petition.setLikeCount(petition.getLikeCount() + 1);
            petition.setDislikeCount(petition.getDislikeCount() - 1);
        }
        // 아무것도 눌려있지 않지만, 좋아요를 누른 기록이 있는 상태
        else if (likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, true).isPresent()) {
            likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, true).get().revive();

            petition.setLikeCount(petition.getLikeCount() + 1);
        }
        // 아무것도 없던 상태 : 좋아요 추가 -> 좋아요 +1
        else {
            LikePetition likePetition = LikePetition.builder().petition(petition).user(user).build();
            likePetitionRepository.save(likePetition);

            petition.setLikeCount(petition.getLikeCount() + 1);
        }
        LikeDislikeResDto result = LikeDislikeResDto.builder()
            .id(petitionId)
            .isLike(likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent())
            .isDislike(dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent())
            .likeCount(petition.getLikeCount())
            .dislikeCount(petition.getDislikeCount())
            .build();

        return result;
    }

    // 게시글 싫어요, 싫어요 취소
    // 싫어요 클릭
    public LikeDislikeResDto dislikePetition(Long petitionId, Long userId) {
        Petition petition = petitionRepository.findById(petitionId).get();
        User user = usersRepository.findById(userId).get();
        
        // 좋아요가 눌려있던 상태 : 좋아요를 취소, 싫어요 추가 -> 좋아요 -1, 싫어요 + 1
        if (likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent()) {
            likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).get().delete();
            DislikePetition dislikePetition = DislikePetition.builder().user(user).petition(petition).build();
            dislikePetitionRepository.save(dislikePetition);
            
            petition.setLikeCount(petition.getLikeCount() - 1);
            petition.setDislikeCount(petition.getDislikeCount() + 1);
        }
        // 싫어요가 이미 눌려있던 상태 : 싫어요 취소 -> 싫어요 -1
        else if (dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent()) {
            dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).get().delete();

            petition.setDislikeCount(petition.getDislikeCount() - 1);
        }
        // 아무것도 눌려있지 않지만, 싫어요를 누른 기록이 있는 상태
        else if (dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, true).isPresent()) {
            dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, true).get().revive();

            petition.setDislikeCount(petition.getDislikeCount() + 1);
        }
        // 아무것도 없던 상태 : 싫어요 추가 -> 싫어요 +1
        else  {
            DislikePetition dislikePetition = DislikePetition.builder().user(user).petition(petition).build();
            dislikePetitionRepository.save(dislikePetition);

            petition.setDislikeCount(petition.getDislikeCount() + 1);
        }
        LikeDislikeResDto result = LikeDislikeResDto.builder()
            .id(petitionId)
            .isLike(likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent())
            .isDislike(dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent())
            .likeCount(petition.getLikeCount())
            .dislikeCount(petition.getDislikeCount())
            .build();

        return result;
    }

    // 댓글 좋아요 좋아요 취소
    // 댓글 좋아요 클릭
    public LikeDislikeResDto likePetitionReply(Long replyId, Long userId) {
        PetitionReply reply = petitionReplyRepository.findById(replyId).get();
        User user = usersRepository.findById(userId).get();
        
        // 이미 좋아요를 누른 상태 : 좋아요를 취소 -> 좋아요 -1
        if (likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent()) {
            likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).get().delete();
            reply.setLikeCount(reply.getLikeCount() - 1);
        }
        // 싫어요가 눌려있던 상태 : 좋아요 추가, 싫어요 취소 -> 좋아요 +1, 싫어요 -1
        else if (dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent()) {
            dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).get().delete();
            LikePetitionReply likePetitionReply = LikePetitionReply.builder().petitionReply(reply).user(user).build();
            likePetitionReplyRepository.save(likePetitionReply);

            reply.setLikeCount(reply.getLikeCount() + 1);
            reply.setDislikeCount(reply.getDislikeCount() - 1);
        }
        // 아무것도 눌려있지 않지만, 좋아요를 누른 기록이 있는 상태
        else if (likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, true).isPresent()) {
            likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, true).get().revive();

            reply.setLikeCount(reply.getLikeCount() + 1);
        }
        // 아무것도 없던 상태 : 좋아요 추가 -> 좋아요 +1
        else {
            LikePetitionReply likePetitionReply = LikePetitionReply.builder().petitionReply(reply).user(user).build();
            likePetitionReplyRepository.save(likePetitionReply);

            reply.setLikeCount(reply.getLikeCount() + 1);
        }
        LikeDislikeResDto result = LikeDislikeResDto.builder()
            .id(replyId)
            .isLike(likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent())
            .isDislike(dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent())
            .likeCount(reply.getLikeCount())
            .dislikeCount(reply.getDislikeCount())
            .build();

        return result;
    }

    // 댓글 싫어요 싫어요 취소
    // 댓글 싫어요 클릭
    public LikeDislikeResDto dislikePetitionReply(Long replyId, Long userId) {
        PetitionReply reply = petitionReplyRepository.findById(replyId).get();
        User user = usersRepository.findById(userId).get();
        
        // 좋아요가 눌려있던 상태 : 좋아요를 취소, 싫어요 추가 -> 좋아요 -1, 싫어요 + 1
        if (likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent()) {
            likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).get().delete();
            DislikePetitionReply dislikePetitionReply = DislikePetitionReply.builder().user(user).petitionReply(reply).build();
            dislikePetitionReplyRepository.save(dislikePetitionReply);
            
            reply.setLikeCount(reply.getLikeCount() - 1);
            reply.setDislikeCount(reply.getDislikeCount() + 1);
        }
        // 싫어요가 이미 눌려있던 상태 : 싫어요 취소 -> 싫어요 -1
        else if (dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent()) {
            dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).get().delete();

            reply.setDislikeCount(reply.getDislikeCount() - 1);
        }
        // 아무것도 눌려있지 않지만, 싫어요를 누른 기록이 있는 상태 : 상태값 true로 변경
        else if (dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, true).isPresent()) {
            dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, true).get().revive();

            reply.setDislikeCount(reply.getDislikeCount() + 1);
        }
        // 아무것도 없던 상태 : 싫어요 추가 -> 싫어요 +1
        else {
            DislikePetitionReply dislikePetitionReply = DislikePetitionReply.builder().user(user).petitionReply(reply).build();
            dislikePetitionReplyRepository.save(dislikePetitionReply);

            reply.setDislikeCount(reply.getDislikeCount() + 1);
        }
        LikeDislikeResDto result = LikeDislikeResDto.builder()
            .id(replyId)
            .isLike(likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent())
            .isDislike(dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent())
            .likeCount(reply.getLikeCount())
            .dislikeCount(reply.getDislikeCount())
            .build();

        return result;
    }

    // 청원 참여
    public ReadPetitionDto supportingPetition(Long petitionId, Long userId) {

        Petition petition = petitionRepository.findById(petitionId).get();
        User user = usersRepository.findById(userId).get();

        if (supportPetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent()) {
            throw new RuntimeException("이미 참여한 청원입니다.");
        }

        SupportPetition supportPetition = SupportPetition.builder()
        .petition(petition)
        .user(user)
        .build();
        supportPetitionRepository.save(supportPetition);

        petition.setSupportCount(petition.getSupportCount() + 1);

        return readPetition(petitionId, userId);
    }

    // 청원 참여 회원 리스트 조회
    // 리턴 값 몰라서 미룸

}
