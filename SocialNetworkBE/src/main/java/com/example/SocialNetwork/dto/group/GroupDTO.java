package com.example.SocialNetwork.dto.group;

import com.example.SocialNetwork.entity.group.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {
    Long id;
    String name;
    String description;
    String imageUrl;
    Long createUserId;
    LocalDateTime createTime;
    Long totalMember;
    Long totalPending;
    Long totalRequest;

    public GroupDTO (Group group){
        this.id = group.getId();
        this.name = group.getName();
        this.description = group.getDescription();
        this.imageUrl = group.getImageUrl();
        this.createUserId = group.getCreateUserId();
        this.createTime = group.getCreateTime();
    }
}
