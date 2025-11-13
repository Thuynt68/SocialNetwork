package com.example.SocialNetwork.service.post;

import com.example.SocialNetwork.dto.post.PostDTO;
import com.example.SocialNetwork.entity.Post;
import com.example.SocialNetwork.entity.PostStatus;
import com.example.SocialNetwork.entity.User;
import com.example.SocialNetwork.repository.post.PostRepo;
import com.example.SocialNetwork.service.image.ImageService;
import com.example.SocialNetwork.service.notification.NotificationService;
import com.example.SocialNetwork.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepo postRepo;
    UserService userService;
    ImageService imageService;
    NotificationService notificationService;

    public Post getById(Long id) {
        return postRepo.findById(id).orElse(null);
    }

    public List<PostDTO> getMyPost() {
        List<Post> posts = postRepo.findAllPost(PostStatus.APPROVED);
        return posts.stream().map(PostDTO::new).toList();
    }

    public PostDTO createPost(PostDTO request) {
        User requestor = userService.getCurrentUser();
        Post post = Post.builder()
                .content(request.getContent())
                .author(requestor)
                .createdTime(LocalDateTime.now())
                .approvalStatus(request.getApprovalStatus())
                .build();

        post = postRepo.save(post);
        return new PostDTO(post);
    }

    public PostDTO updatePost(PostDTO request) {
        Post existingPost = getById(request.getId());
        existingPost.setContent(request.getContent());
        existingPost.setApprovalStatus(request.getApprovalStatus());
        postRepo.save(existingPost);
        imageService.updatePostImages(request, existingPost);
        return new PostDTO(existingPost);
    }

    public void deletePost(Long id) {
        postRepo.deleteById(id);
    }


    public void approvePost(Long postId, PostStatus approvalStatus) {
        Post post = getById(postId);
        User requestor = userService.getCurrentUser();
        if (approvalStatus.equals(PostStatus.APPROVED)){
            post.setApprovalStatus(PostStatus.APPROVED);
            post = postRepo.save(post);
        }
        else {
            postRepo.delete(post);
        }
    }

    public List<PostDTO> getPostsByUserId(Long userId) {
        User user = userService.getById(userId);
        List<Post> posts = postRepo.findByAuthor(user);
        return posts.stream().map(PostDTO::new).toList();
    }
}
