package com.example.SocialNetwork.service.post;


import com.example.SocialNetwork.dto.post.CommentDTO;
import com.example.SocialNetwork.entity.Comment;
import com.example.SocialNetwork.entity.Post;
import com.example.SocialNetwork.entity.User;
import com.example.SocialNetwork.repository.post.CommentRepo;
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
public class CommentService {
    CommentRepo commentRepo;
    UserService userService;
    PostService postService;

    public Comment getById(Long id) {
        return commentRepo.findById(id).orElse(null);
    }

    public CommentDTO createComment(CommentDTO request) {
        User currentUser = userService.getCurrentUser();
        Post post = postService.getById(request.getPostId());
        Comment comment = Comment.builder()
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .author(currentUser)
                .post(post)
                .time(LocalDateTime.now())
                .build();
        comment = commentRepo.save(comment);
        return new CommentDTO(comment);
    }

    public Boolean updateComment(CommentDTO request) {
        Comment existingComment = commentRepo.getById(request.getId());
        existingComment.setContent(request.getContent());
        existingComment.setImageUrl(request.getImageUrl());
        existingComment.setTime(LocalDateTime.now());
        commentRepo.save(existingComment);
        return true;
    }

    public void deleteComment(Long commentId) {
        commentRepo.deleteById(commentId);
    }

    public List<CommentDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        return comments.stream()
                .map(CommentDTO::new)
                .toList();
    }

    public void deleteAllCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        commentRepo.deleteAll(comments);
    }

    public void deleteByPost(Post post) {
        commentRepo.deleteByPost(post);
    }
}




