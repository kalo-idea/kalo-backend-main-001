package kalo.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.domain.Hashtags;
import kalo.main.domain.Posts;
import kalo.main.domain.PostsHashtags;
import kalo.main.domain.dto.posts.CreatePostsDto;
import kalo.main.domain.dto.posts.GetPostsWriter;
import kalo.main.domain.dto.posts.ViewPostsDto;
import kalo.main.repository.DislikePostsRepository;
import kalo.main.repository.HashtagsRepository;
import kalo.main.repository.LikePostsRepository;
import kalo.main.repository.PostsHashtagsRepository;
import kalo.main.repository.PostsRepository;
import kalo.main.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostsService {
    
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;
    private final HashtagsRepository hashtagsRepository;
    private final PostsHashtagsRepository postsHashtagsRepository;
    private final LikePostsRepository likePostsRepository;
    private final DislikePostsRepository dislikePostsRepository;


    public Long createPosts(CreatePostsDto createPostsDto) {

        Posts posts = Posts.builder()
        .title(createPostsDto.getTitle())
        .content(createPostsDto.getContent())
        .photos(createPostsDto.getPhotos())
        .viewCount(0L)
        .users(usersRepository.findById(createPostsDto.getUsersId()).get())
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

        Posts resultPost = postsRepository.save(posts);

        List<String> hashtags = createPostsDto.getHashtags();
        for (String hashtag : hashtags) {
            try {
                // hashtag find
                PostsHashtags postsHashtags = PostsHashtags.builder()
                    .posts(resultPost)
                    .hashtags(hashtagsRepository.findByWord(hashtag).orElseThrow())
                    .build();
                postsHashtagsRepository.save(postsHashtags);
            } catch(NoSuchElementException e) {
                // hashtag make
                Hashtags makeHashtag = new Hashtags(hashtag);
                hashtagsRepository.save(makeHashtag);
                PostsHashtags postsHashtags = PostsHashtags.builder()
                    .posts(resultPost)
                    .hashtags(makeHashtag)
                    .build();
                postsHashtagsRepository.save(postsHashtags);
            }
        }

        return resultPost.getId();
    }

    public ViewPostsDto viewPosts(Long postId, Long viewerId) {
        Boolean isLike = likePostsRepository.findByPostsIdAndUsersId(postId, viewerId).isPresent();
        Boolean isDislike = dislikePostsRepository.findByPostsIdAndUsersId(postId, viewerId).isPresent();
        GetPostsWriter findPost = postsRepository.findWriter(postId);
        Posts post = postsRepository.findById(postId).get();
        List<Hashtags> hashs = hashtagsRepository.findPostsHashtags(postId);

        List<String> hashtags = new ArrayList();

        for (Hashtags hash : hashs) {
            hashtags.add(hash.getWord());
        }
        post.setViewCount(post.getViewCount() + 1);

        return ViewPostsDto.builder()
        .userId(findPost.getUserId())
        .nickname(findPost.getNickname())
        .profileSrc(findPost.getProfileSrc())
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
