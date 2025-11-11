package com.example.SocialNetwork.dto.post;


import com.example.SocialNetwork.dto.user.UserDTO;
import com.example.SocialNetwork.entity.Post;
import com.example.SocialNetwork.entity.PostStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDTO {
    Long id;
    String content;
    List<ImageDTO> images;
    List<ImageDTO> newImages;
    List<ImageDTO> deleteImages;
    LocalDateTime time;
    UserDTO author;
    Long groupId;
    String groupName;
    PostStatus approvalStatus;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.images = Optional.ofNullable(post.getImageList())
                .orElseGet(ArrayList::new).stream().map(ImageDTO::new).toList();
        this.time = post.getCreatedTime();
        if (post.getGroup() != null){
            this.groupId = post.getGroup().getId();
            this.groupName = post.getGroup().getName();
        }

        this.author = new UserDTO(post.getAuthor());
        this.approvalStatus = post.getApprovalStatus();
    }
}
