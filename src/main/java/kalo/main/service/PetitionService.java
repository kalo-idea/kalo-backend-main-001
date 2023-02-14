package kalo.main.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.controller.BasicException;
import kalo.main.domain.DislikePetition;
import kalo.main.domain.DislikePetitionReply;
import kalo.main.domain.Hashtag;
import kalo.main.domain.Ledger;
import kalo.main.domain.LikePetition;
import kalo.main.domain.LikePetitionReply;
import kalo.main.domain.Media;
import kalo.main.domain.MediaPetition;
import kalo.main.domain.Petition;
import kalo.main.domain.PetitionHashtag;
import kalo.main.domain.PetitionReply;
import kalo.main.domain.SupportPetition;
import kalo.main.domain.User;
import kalo.main.domain.dto.LikeDislikeResDto;
import kalo.main.domain.dto.ReplyDto;
import kalo.main.domain.dto.SimpleDeletedWriterDto;
import kalo.main.domain.dto.SimpleWriterDto;
import kalo.main.domain.dto.TargetIdUserIdDto;
import kalo.main.domain.dto.petition.CreatePetitionDto;
import kalo.main.domain.dto.petition.CreatePetitionReplyDto;
import kalo.main.domain.dto.petition.ImportantPetitionResDto;
import kalo.main.domain.dto.petition.PetitionCondDto;
import kalo.main.domain.dto.petition.ReadPetitionDto;
import kalo.main.domain.dto.petition.ReadPetitionsDto;
import kalo.main.domain.dto.petition.ReadSimplePetitionsDto;
import kalo.main.domain.dto.petition.SimpleImportantPetitionDto;
import kalo.main.domain.dto.petition.SupportPetitionUserListDto;
import kalo.main.repository.DislikePetitionReplyRepository;
import kalo.main.repository.DislikePetitionRepository;
import kalo.main.repository.HashtagRepository;
import kalo.main.repository.LedgerRepository;
import kalo.main.repository.LikePetitionReplyRepository;
import kalo.main.repository.LikePetitionRepository;
import kalo.main.repository.MediaPetitionRepository;
import kalo.main.repository.MediaRepository;
import kalo.main.repository.PetitionHashtagRepository;
import kalo.main.repository.PetitionReplyRepository;
import kalo.main.repository.PetitionRepository;
import kalo.main.repository.SupportPetitionRepository;
import kalo.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PetitionService {

    private final PetitionRepository petitionRepository;
    private final UserRepository userRepository;
    private final PetitionHashtagRepository petitionHashtagRepository;
    private final HashtagRepository hashtagRepository;
    private final LikePetitionRepository likePetitionRepository;
    private final DislikePetitionRepository dislikePetitionRepository;
    private final PetitionReplyRepository petitionReplyRepository;
    private final LikePetitionReplyRepository likePetitionReplyRepository;
    private final DislikePetitionReplyRepository dislikePetitionReplyRepository;
    private final SupportPetitionRepository supportPetitionRepository;
    private final MediaRepository mediaRepository;
    private final MediaPetitionRepository mediaPetitionRepository;
    private final LedgerRepository ledgerRepository;
    private final LedgerService ledgerService;

    // 청원 생성
    public Long createPetition(CreatePetitionDto createPetitionDto) {
        
        Petition petition = Petition.builder()  
        .title(createPetitionDto.getTitle())
        .content(createPetitionDto.getContent())
        .supportCount(0L)
        .viewCount(0L)
        .user(userRepository.findById(createPetitionDto.getWriterId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다.")))
        .progress("unchecked")
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
        .supportingDateEnd(LocalDate.now().plusDays(29).atStartOfDay())
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

        List<String> medium = createPetitionDto.getMedium();
        for (String fileName : medium) {
            Media media = new Media(fileName);
            mediaRepository.save(media);
            MediaPetition mediaPetition = MediaPetition.builder()
                .media(media)
                .petition(petition)
                .build();
            mediaPetitionRepository.save(mediaPetition);
        }

        return resultPetition.getId();
    }

    // 청원 단건 조회
    public ReadPetitionDto readPetition(Long petitionId, Long viewerId) {

        Petition petition = petitionRepository.findById(petitionId).orElseThrow(() -> new BasicException("청원을 찾을 수 없습니다."));
        
        Boolean isLike = false;
        Boolean isDislike = false;
        Boolean isSupport = false;
        if (viewerId != null) { // 로그인의 경우 좋아요, 싫어요 여부 확인
            isLike = likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, viewerId, false).isPresent();
            isDislike = dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, viewerId, false).isPresent();
            isSupport = supportPetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, viewerId, false).isPresent();
        }
        
        // 삭제된 청원이면 널 반환
        if (petition.getDeleted()) {
            throw new BasicException("삭제된 청원입니다.");
        }

        List<Hashtag> hashtags = hashtagRepository.findPetitionHashtags(petitionId);
        List<String> hashtags_res = new ArrayList<String>();

        for (Hashtag hashtag : hashtags) {
            hashtags_res.add(hashtag.getWord());
        }

        petition.setViewCount(petition.getViewCount() + 1);

        List<Media> medium = mediaRepository.findPetitionMedia(petitionId);
        List<String> medium_res = new ArrayList<String>();

        for (Media media : medium) {
            medium_res.add(media.getFileName());
        }

        String progress = petition.getProgress();
        if (progress.equals("unchecked")) {
            if (LocalDateTime.now().isAfter(petition.getSupportingDateEnd())) {
                if (petition.getSupportCount() >= petition.getGoal()) {
                    progress = "ongoing";
                } else {
                    progress = "fail";
                }
            } else {
                progress = "recruit";
            }
        }

        List<String> steps = Arrays.asList(petition.getStep().split(","));

        User user = userRepository.findById(petition.getUser().getId()).orElseThrow(() -> new BasicException("작성자를 찾을 수 없습니다."));
        SimpleWriterDto writer = !user.getDeleted() ? new SimpleWriterDto(user) : new SimpleDeletedWriterDto();

        SimpleImportantPetitionDto importantInfo = new SimpleImportantPetitionDto();
        if (petition.getImportantPetition() != null) {
            importantInfo.setImageSrc(petition.getImportantPetition().getImageSrc());
            importantInfo.setContent(petition.getImportantPetition().getContent());
        } else {
            importantInfo = null;
        }
        
        return ReadPetitionDto.builder()
        .writer(writer)
        .id(petition.getId())
        .title(petition.getTitle())
        .createdDate(petition.getCreatedDate())
        .important(importantInfo)
        .content(petition.getContent())
        .hashtags(hashtags_res)
        .medium(medium_res)
        .likeCount(petition.getLikeCount())
        .isLike(isLike)
        .dislikeCount(petition.getDislikeCount())
        .isDislike(isDislike)
        .progress(progress)
        .step(steps)
        .goal(petition.getGoal())
        .replyCount(petition.getReplyCount())
        .category(petition.getCategory())
        .region1depthName(petition.getRegion1depthName())
        .region2depthName(petition.getRegion2depthName())
        .latitude(petition.getLatitude())
        .longitude(petition.getLongitude())
        .supportCount(petition.getSupportCount())
        .isSupport(isSupport)
        .supportingDateEnd(petition.getSupportingDateEnd())
        .build();
    }

    // 청원 댓글 추가
    public Long createPetitionReply(CreatePetitionReplyDto createPetitionReplyDto) {
        Petition petition = petitionRepository.findById(createPetitionReplyDto.getPetitionId()).orElseThrow(() -> new BasicException("없는 청원입니다."));
        petition.setReplyCount(petition.getReplyCount() + 1);

        PetitionReply petitionReply = PetitionReply.builder()
        .user(userRepository.findById(createPetitionReplyDto.getUserId()).orElseThrow(() -> new BasicException("없는 회원입니다.")))
        .content(createPetitionReplyDto.getContent())
        .petition(petition)
        .likeCount(0L)
        .dislikeCount(0L)
        .build();

        PetitionReply result = petitionReplyRepository.save(petitionReply);

        return result.getId();
    }


    // 청원 댓글 조회
    public List<ReplyDto> readPetitionsReply(Long petitionId, Long viewerId, Pageable pageable) {

        List<PetitionReply> replys = petitionReplyRepository.findByPetitionIdAndDeleted(petitionId, pageable, false);

        List<ReplyDto> result = new ArrayList<ReplyDto>();

        for (PetitionReply reply : replys) {
            Boolean isLike = false;
            Boolean isDislike = false;
            if (viewerId != null) {
                isLike = likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(reply.getId(), viewerId, false).isPresent();
                isDislike = dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(reply.getId(), viewerId, false).isPresent();
            }

            SimpleWriterDto writer = !reply.getUser().getDeleted() ? new SimpleWriterDto(reply.getUser()) : new SimpleDeletedWriterDto();
            ReplyDto commentDto = ReplyDto.builder()
            .id(reply.getId())
            .writer(writer)
            .createdDate(reply.getCreatedDate())
            .isLike(isLike)
            .likeCount(reply.getLikeCount())
            .isDislike(isDislike)
            .dislikeCount(reply.getDislikeCount())
            .content(reply.getContent())
            .build();

            result.add(commentDto);
        }
        return result;
    }

    // 청원 리스트 조회
    public List<ReadPetitionsDto> readPetitions(Pageable pageable, PetitionCondDto cond, Boolean recent) {

        List<ReadSimplePetitionsDto> simplePetitions = petitionRepository.findListPetitions(pageable, cond, recent);

        List<ReadPetitionsDto> result = new ArrayList<>();
        for (ReadSimplePetitionsDto simplePetition : simplePetitions) {
            
            String progress = simplePetition.getProgress();
            if (progress.equals("unchecked")) {
                if (LocalDateTime.now().isAfter(simplePetition.getSupportingDateEnd())) {
                    if (simplePetition.getSupportCount() >= simplePetition.getGoal()) {
                        progress = "ongoing";
                    } else {
                        progress = "fail";
                    }
                } else {
                    progress = "recruit";
                }
            }

            simplePetition.setProgress(progress);

            List<String> steps = Arrays.asList(simplePetition.getStep().split(","));
            
            List<String> words = new ArrayList<String>();
            List<Hashtag> hashtags = hashtagRepository.findPetitionHashtags(simplePetition.getPetitionId());
            for (Hashtag hashtag : hashtags) {
                words.add(hashtag.getWord());
            }


            List<String> fileNames = new ArrayList<String>();
            List<Media> medium = mediaRepository.findPetitionMedia(simplePetition.getPetitionId());
            for (Media media : medium) {
                fileNames.add(media.getFileName());
            }
            System.out.println("");

            User user = userRepository.findById(simplePetition.getWriterId()).orElseThrow(() -> new BasicException("작성자를 찾을 수 없습니다."));
            SimpleWriterDto writer = !user.getDeleted() ? new SimpleWriterDto(user) : new SimpleDeletedWriterDto();
            
            result.add(new ReadPetitionsDto(simplePetition, writer, steps, words, fileNames));
        }

        return result;
    }

    // 청원 좋아요, 좋아요 취소
    // 좋아요 클릭
    public LikeDislikeResDto likePetition(TargetIdUserIdDto req) 
    {
        Long petitionId = req.getTargetId();
        Long userId = req.getUserId();

        Petition petition = petitionRepository.findById(petitionId).orElseThrow(() -> new BasicException("청원을 찾을 수 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        
        // 좋아요가 눌려있으면 like -1
        if (likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent()) {
            likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).get().delete();

            petition.setLikeCount(petition.getLikeCount() - 1);
        }
        // like +1
        else {
            // 싫어요가 눌려있으면 dislike -1
            if (dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent()) {
                dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).get().delete();
                
                petition.setDislikeCount(petition.getDislikeCount() - 1);
            }

            // like 추가
            if (likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, true).isPresent()) {
                likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, true).get().revive();

            }
            else {
                LikePetition likePetition = LikePetition.builder().petition(petition).user(user).build();
                likePetitionRepository.save(likePetition);
            }

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

    // 청원 싫어요, 싫어요 취소
    // 싫어요 클릭
    public LikeDislikeResDto dislikePetition(TargetIdUserIdDto req) {
        Long petitionId = req.getTargetId();
        Long userId = req.getUserId();

        Petition petition = petitionRepository.findById(petitionId).orElseThrow(() -> new BasicException("청원을 찾을 수 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));;
        
        // 싫어요가 눌려있으면 dislike -1
        if (dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent()) {
            dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).get().delete();

            petition.setDislikeCount(petition.getDislikeCount() - 1);
        }
        // like +1
        else {
            // 좋아요가 눌려있으면 like -1
            if (likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent()) {
                likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).get().delete();
                
                petition.setLikeCount(petition.getLikeCount() - 1);
            }

            // dislike 추가
            if (dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, true).isPresent()) {
                dislikePetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, true).get().revive();

            }
            else {
                DislikePetition dislikePetition = DislikePetition.builder().petition(petition).user(user).build();
                dislikePetitionRepository.save(dislikePetition);
            }

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
    public LikeDislikeResDto likePetitionReply(TargetIdUserIdDto req) {
        Long replyId = req.getTargetId();
        Long userId = req.getUserId();

        PetitionReply reply = petitionReplyRepository.findById(replyId).orElseThrow(() -> new BasicException("댓글을 찾을 수 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        
        // 좋아요가 눌려있으면 like -1
        if (likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent()) {
            likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).get().delete();

            reply.setLikeCount(reply.getLikeCount() - 1);
        }
        // like +1
        else {
            // 싫어요가 눌려있으면 dislike -1
            if (dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent()) {
                dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).get().delete();
                
                reply.setDislikeCount(reply.getDislikeCount() - 1);
            }

            // like 추가
            if (likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, true).isPresent()) {
                likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, true).get().revive();

            }
            else {
                LikePetitionReply likePetitionReply = LikePetitionReply.builder().petitionReply(reply).user(user).build();
                likePetitionReplyRepository.save(likePetitionReply);
            }

            reply.setLikeCount(reply.getLikeCount() + 1);
        }

        LikeDislikeResDto result = LikeDislikeResDto.builder()
            .id(replyId)
            .isLike(likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent())
            .isDislike(dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent())
            .likeCount(reply.getLikeCount())
            .dislikeCount(reply.getDislikeCount())
            .build();
        
        System.out.println("aaaa");

        return result;
    }

    // 댓글 싫어요 싫어요 취소
    // 댓글 싫어요 클릭
    public LikeDislikeResDto dislikePetitionReply(TargetIdUserIdDto req) {
        Long replyId = req.getTargetId();
        Long userId = req.getUserId();
        
        PetitionReply reply = petitionReplyRepository.findById(replyId).orElseThrow(() -> new BasicException("댓글을 찾을 수 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        
        // 싫어요가 눌려있으면 dislike -1
        if (dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent()) {
            dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).get().delete();

            reply.setDislikeCount(reply.getDislikeCount() - 1);
        }
        // like +1
        else {
            // 좋아요가 눌려있으면 like -1
            if (likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent()) {
                likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, false).get().delete();
                
                reply.setLikeCount(reply.getLikeCount() - 1);
            }

            // dislike 추가
            if (dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, true).isPresent()) {
                dislikePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(replyId, userId, true).get().revive();

            }
            else {
                DislikePetitionReply dislikePetitionReply = DislikePetitionReply.builder().petitionReply(reply).user(user).build();
                dislikePetitionReplyRepository.save(dislikePetitionReply);
            }

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

        Petition petition = petitionRepository.findById(petitionId).orElseThrow(() -> new BasicException("청원을 찾을 수 없습니다."));
        
        if (LocalDateTime.now().isAfter(petition.getSupportingDateEnd())) {
            throw new BasicException("참여 가능한 시간이 지났습니다.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));

        if (supportPetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).isPresent()) {
            throw new BasicException("이미 참여한 청원입니다.");
        }
        Long getSum = ledgerService.getPoint(userId);
        if (getSum < 500) {
            throw new BasicException("포인트가 부족합니다.");
        }
        Ledger ledger = Ledger.builder().user(user).type("support").amount(-500L).build();
        ledgerRepository.save(ledger);

        SupportPetition supportPetition = SupportPetition.builder()
        .petition(petition)
        .user(user)
        .build();
        supportPetitionRepository.save(supportPetition);

        petition.setSupportCount(petition.getSupportCount() + 1);

        return readPetition(petitionId, userId);
    }

    // 청원 참여 회원 리스트 조회
    public List<SupportPetitionUserListDto> getSupportPetitionList(Pageable pageable, Long petitionId) {
        List<SupportPetitionUserListDto> res = petitionRepository.findSupportPetitionUserList(pageable, petitionId);

        for (SupportPetitionUserListDto supportPetitionUser : res) {
            String nickname = supportPetitionUser.getNickname();
            int len = nickname.length() - 1;
            
            String convertNickname = nickname.substring(0, 1);
            while(len-- > 0) {
                convertNickname += "*";
            }
            supportPetitionUser.setNickname(convertNickname);
            supportPetitionUser.setCreatedDate(supportPetitionUser.getCreatedDate().toLocalDate().atStartOfDay());
        }
        return res;
    }

    public List<ReadPetitionsDto> getPetitionsByHashtag(Pageable pageable, String hash) {

        List<ReadSimplePetitionsDto> simplePetitions = hashtagRepository.getPetitionsByHashtag(pageable, hash);

        List<ReadPetitionsDto> result = new ArrayList<>();
        for (ReadSimplePetitionsDto simplePetition : simplePetitions) {
            
            String progress = simplePetition.getProgress();
            if (progress.equals("unchecked")) {
                if (LocalDateTime.now().isAfter(simplePetition.getSupportingDateEnd())) {
                    if (simplePetition.getSupportCount() >= simplePetition.getGoal()) {
                        progress = "ongoing";
                    } else {
                        progress = "fail";
                    }
                } else {
                    progress = "recruit";
                }
            }

            simplePetition.setProgress(progress);

            List<String> steps = Arrays.asList(simplePetition.getStep().split(","));
            
            List<String> words = new ArrayList<String>();
            List<Hashtag> hashtags = hashtagRepository.findPetitionHashtags(simplePetition.getPetitionId());
            for (Hashtag hashtag : hashtags) {
                words.add(hashtag.getWord());
            }


            List<String> fileNames = new ArrayList<String>();
            List<Media> medium = mediaRepository.findPetitionMedia(simplePetition.getPetitionId());
            for (Media media : medium) {
                fileNames.add(media.getFileName());
            }
            System.out.println("");

            User user = userRepository.findById(simplePetition.getWriterId()).orElseThrow(() -> new BasicException("작성자를 찾을 수 없습니다."));
            SimpleWriterDto writer = !user.getDeleted() ? new SimpleWriterDto(user) : new SimpleDeletedWriterDto();
            
            result.add(new ReadPetitionsDto(simplePetition, writer, steps, words, fileNames));
        }

        return result;
    }

    // 중요청원 출력
    public List<ImportantPetitionResDto> getImportantPetitons(Pageable pageable) {
        return petitionRepository.getImportantPetitions(pageable);
    }
}
