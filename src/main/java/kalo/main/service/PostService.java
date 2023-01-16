package kalo.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.domain.Hashtag;
import kalo.main.domain.Post;
import kalo.main.domain.PostHashtag;
import kalo.main.domain.User;
import kalo.main.domain.dto.post.CreatePostDto;
import kalo.main.domain.dto.post.ReadPostDto;
import kalo.main.repository.DislikePostRepository;
import kalo.main.repository.HashtagRepository;
import kalo.main.repository.LikePostRepository;
import kalo.main.repository.PostHashtagRepository;
import kalo.main.repository.PostRepository;
import kalo.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    
    private final UserRepository usersRepository;
    private final PostRepository postsRepository;
    private final HashtagRepository hashtagsRepository;
    private final PostHashtagRepository postsHashtagsRepository;
    private final LikePostRepository likePostsRepository;
    private final DislikePostRepository dislikePostsRepository;


    public Long createPost(CreatePostDto createPostsDto) {

        Post posts = Post.builder()
        .title(createPostsDto.getTitle())
        .content(createPostsDto.getContent())
        .photos(createPostsDto.getPhotos())
        .viewCount(0L)
        .user(usersRepository.findById(createPostsDto.getUserId()).get())
        .replyCount(0L)
        .likeCount(0L)
        .dislikeCount(0L)
        .topic(createPostsDto.getTopic())
        .addressName(createPostsDto.getAddressName())
        .region1depthName(createPostsDto.getRegion1depthName())
        .region2depthName(createPostsDto.getRegion2depthName())
        .region3depthName(createPostsDto.getRegion3depthName())
        .latitude(createPostsDto.getLatitude())
        .longitude(createPostsDto.getLongitude())
        .build();

        Post resultPost = postsRepository.save(posts);

        List<String> hashtags = createPostsDto.getHashtags();
        for (String hashtag : hashtags) {
            try {
                // hashtag find
                PostHashtag postsHashtags = PostHashtag.builder()
                    .post(resultPost)
                    .hashtag(hashtagsRepository.findByWordAndDeleted(hashtag, false).orElseThrow())
                    .build();
                postsHashtagsRepository.save(postsHashtags);
            } catch(NoSuchElementException e) {
                // hashtag make
                Hashtag makeHashtag = new Hashtag(hashtag);
                hashtagsRepository.save(makeHashtag);
                PostHashtag postsHashtags = PostHashtag.builder()
                    .post(resultPost)
                    .hashtag(makeHashtag)
                    .build();
                postsHashtagsRepository.save(postsHashtags);
            }
        }

        return resultPost.getId();
    }

    public ReadPostDto viewPosts(Long postId, Long viewerId) {
        Boolean isLike = likePostsRepository.findByPostIdAndUserIdAndDeleted(postId, viewerId, false).isPresent();
        Boolean isDislike = dislikePostsRepository.findByPostIdAndUserIdAndDeleted(postId, viewerId, false).isPresent();
        Post post = postsRepository.findById(postId).get();
        User writer = usersRepository.findById(post.getUser().getId()).get();
        List<Hashtag> hashs = hashtagsRepository.findPostHashtags(postId);
        List<String> hashtags = new ArrayList();

        for (Hashtag hash : hashs) {
            hashtags.add(hash.getWord());
        }
        
        post.setViewCount(post.getViewCount() + 1);

        return ReadPostDto.builder()
        .userId(writer.getId())
        .nickname(writer.getNickname())
        .profileSrc(writer.getProfileSrc())
        .title(post.getTitle())
        .createdDate(post.getCreatedDate())
        .content(post.getContent())
        .hashtags(hashtags)
        .photos(post.getPhotos())
        .likeCount(post.getLikeCount())
        .isLike(isLike)
        .dislikeCount(post.getDislikeCount())
        .isDislike(isDislike)
        .replyCount(post.getReplyCount())
        .topic(post.getTopic())
        .region1depthName(post.getRegion1depthName())
        .region2depthName(post.getRegion2depthName())
        .build();
    }
}