package kalo.main.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kalo.main.domain.Hashtag;
import kalo.main.domain.User;
import kalo.main.domain.dto.petition.ReadPetitionsDto;
import kalo.main.domain.dto.petition.ReadSimplePetitionsDto;
import kalo.main.repository.HashtagRepository;
import kalo.main.repository.PetitionRepository;
import kalo.main.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;
    private final PetitionRepository petitionRepository;
    private final HashtagRepository hashtagRepository;

    // 유저 청원 좋아요 리스트조회
    public List<ReadPetitionsDto> getLikePetitions(Pageable pageable, Long userId) {
        List<ReadSimplePetitionsDto> simplePetitions = petitionRepository.findLikePetitions(pageable, userId);

        List<ReadPetitionsDto> result = new ArrayList();
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

    // 유저 청원 참여 리스트조회
    public List<ReadPetitionsDto> getSupportPetitions(Pageable pageable, Long userId) {
        List<ReadSimplePetitionsDto> simplePetitions = petitionRepository.findSupportPetitions(pageable, userId);

        List<ReadPetitionsDto> result = new ArrayList();
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
}
