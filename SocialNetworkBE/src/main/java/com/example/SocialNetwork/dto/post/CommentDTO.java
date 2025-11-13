package com.example.SocialNetwork.dto.post;

import com.example.SocialNetwork.entity.Comment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDTO {
    Long id;
    String content;
    String imageUrl;
    Long postId;
    LocalDateTime time;
    String authorUsername;
    String author;
    String authorAvatar;

    public CommentDTO(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.imageUrl = comment.getImageUrl();
        this.postId = comment.getPost().getId();
        this.time = comment.getTime();
        this.authorUsername = comment.getAuthor().getUsername();
        this.author = comment.getAuthor().getFullName();
        this.authorAvatar = comment.getAuthor().getAvatarUrl();
    }
}
