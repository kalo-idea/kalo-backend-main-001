package kalo.main.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.domain.Campaign;
import kalo.main.domain.CampaignGroup;
import kalo.main.domain.Petition;
import kalo.main.domain.SupportPetition;
import kalo.main.domain.dto.OnlyIdDto;
import kalo.main.domain.dto.petition.CreatePetitionDto;
import kalo.main.domain.dto.user.JoinReqDto;
import kalo.main.repository.CampaignGroupRepository;
import kalo.main.repository.CampaignRepository;
import kalo.main.repository.PetitionRepository;
import kalo.main.repository.SupportPetitionRepository;
import kalo.main.repository.UserRepository;

@Transactional
@SpringBootTest
public class ServiceTest {
    
    @Autowired
    UserService usersService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LedgerService ledgerService;
    @Autowired
    PetitionService petitionService;
    @Autowired
    PetitionRepository petitionRepository;
    @Autowired
    CampaignService campaignService;
    @Autowired
    SupportPetitionRepository supportPetitionRepository;
    @Autowired
    CampaignRepository campaignRepository;
    @Autowired
    CampaignGroupRepository campaignGroupRepository;


    @BeforeEach
    void beforeEach() {
        campaignRepository.deleteAll();
        campaignGroupRepository.deleteAll();
    }

    @Test
    void totalTest() {

        Long beforeCount = userRepository.count();

        JoinReqDto joinReq = new JoinReqDto();
        joinReq.setBirth(LocalDate.now());
        joinReq.setEmail("test@naver.com");
        joinReq.setFcmToken("rr");
        joinReq.setGender("man");
        joinReq.setKakao("kakaotest12");
        joinReq.setName("testName");
        joinReq.setPromotionCheck(true);
        joinReq.setRegion1depthName("경기");
        joinReq.setRegion2depthName("안양");
        joinReq.setTel("010-1234-9173");
        joinReq.setNickname("닉닉닉이");
        Long userId = usersService.createAuth(joinReq).getUserInfos().get(0).getUserId();
        Long afterCount = userRepository.count();

        assertThat(beforeCount).isEqualTo(afterCount - 1);

        Long nowPoint = usersService.getProfileHome(userId).getPoint();
        
        assertThat(nowPoint).isEqualTo(0);

        beforeCount = petitionRepository.count();
        CreatePetitionDto createPetition = new CreatePetitionDto();
        createPetition.setTitle("testTitle");
        createPetition.setContent("testContent");
        createPetition.setMedia(new ArrayList<String>());
        createPetition.setId(userId);
        List<String> hashtags = new ArrayList<String>();
        hashtags.add("testHash1");
        hashtags.add("testHash2");
        hashtags.add("testHash3");
        hashtags.add("testHash4");
        createPetition.setHashtags(hashtags);
        createPetition.setAddressName("어딘가");
        createPetition.setRegion1depthName("경기");
        createPetition.setRegion2depthName("안양");
        createPetition.setRegion3depthName("동안구");
        createPetition.setLatitude(37.53362220676191d);
        createPetition.setLongitude(126.97755295059169d);
        createPetition.setCategory("카테고리");
        createPetition.setGoal(100L);
        Long petitionId = petitionService.createPetition(createPetition);
        afterCount = petitionRepository.count();

        assertThat(beforeCount).isEqualTo(afterCount - 1);
        
        Assertions.assertThatThrownBy(() -> petitionService.supportingPetition(petitionId, userId)).hasMessage("포인트가 부족합니다.");
        
        ledgerService.attend(userId);
        nowPoint = usersService.getProfileHome(userId).getPoint();
        assertThat(nowPoint).isEqualTo(500);

        Assertions.assertThatThrownBy(() -> ledgerService.attend(userId)).hasMessage("이미 출석한 회원입니다.");
        nowPoint = usersService.getProfileHome(userId).getPoint();
        assertThat(nowPoint).isEqualTo(500);
        
        Petition findPetition = petitionRepository.findById(petitionId).get();
        findPetition.setCreatedDate(LocalDateTime.now().minusDays(40));
        Assertions.assertThatThrownBy(() -> petitionService.supportingPetition(petitionId, userId)).hasMessage("참여 가능한 시간이 지났습니다.");
        
        findPetition.setCreatedDate(LocalDateTime.now());
        beforeCount = petitionService.readPetition(petitionId, userId).getSupportCount();
        petitionService.supportingPetition(petitionId, userId);
        afterCount = petitionService.readPetition(petitionId, userId).getSupportCount();
        nowPoint = usersService.getProfileHome(userId).getPoint();
        assertThat(nowPoint).isEqualTo(0);
        assertThat(beforeCount).isEqualTo(afterCount - 1);

        CampaignGroup campaignGroup = CampaignGroup.builder()
        .year(LocalDateTime.now().minusMonths(1).getYear())
        .month(LocalDateTime.now().minusMonths(1).getMonthValue())
        .supportingDateStart(LocalDate.of(LocalDate.now().minusMonths(1).getYear(), LocalDate.now().minusMonths(1).getMonthValue(), 1).atStartOfDay())
        .supportingDateEnd(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1).minusDays(1).atTime(LocalTime.MAX))
        .votingDateStart(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1).atStartOfDay())
        .votingDateEnd(LocalDate.of(LocalDate.now().plusMonths(1).getYear(), LocalDate.now().plusMonths(1).getMonthValue(), 1).minusDays(1).atTime(LocalTime.MAX))
        .donation(150000L)
        .build();
        Long campaignGroupId = campaignGroupRepository.save(campaignGroup).getId();

        Campaign campaign = Campaign.builder()
        .title( "캠페인 제목")
        .info("캠페인 정보")
        .thumbnail("캠페인 썸네일")
        .contentImage("캠페인 이미지")
        .vote(0L)
        .campaignGroup(campaignGroup)
        .build();
        Long campaignId = campaignRepository.save(campaign).getId();

        assertThat(campaignService.getVoteCampaignStatus(userId, LocalDate.now().minusMonths(1).getYear(), LocalDate.now().minusMonths(1).getMonthValue())).isEqualTo("impossible");
        
        assertThat(supportPetitionRepository.findByUserIdAndDeletedAndCreatedDateBetween(userId, 
        false,
        campaignGroup.getSupportingDateStart(), 
        campaignGroup.getSupportingDateEnd())).size()
        .isEqualTo(0);

        Long supportPetitionId = supportPetitionRepository.findByPetitionIdAndUserIdAndDeleted(petitionId, userId, false).get().getId();
        

        Assertions.assertThatThrownBy(() -> campaignService.voteCampaign(campaignId, userId)).hasMessage("투표권이 없습니다.");

        findPetition.setCreatedDate(LocalDateTime.now().minusMonths(1));
        SupportPetition supportPetition = supportPetitionRepository.findById(supportPetitionId).get();
        supportPetition.setCreatedDate(LocalDateTime.now().minusMonths(1));
        
        assertThat(campaignService.getVoteCampaignStatus(userId, LocalDate.now().minusMonths(1).getYear(), LocalDate.now().minusMonths(1).getMonthValue())).isEqualTo("possible");

        
        campaignService.voteCampaign(campaignId, userId);
            

        assertThat(campaignService.getVoteCampaignStatus(userId, LocalDate.now().minusMonths(1).getYear(), LocalDate.now().minusMonths(1).getMonthValue())).isEqualTo("complete");
        Assertions.assertThatThrownBy(() -> campaignService.voteCampaign(campaignId, userId)).hasMessage("이미 투표를 하셨습니다.");

    }
}
