package com.example.SocialNetwork.dto.group;

import com.example.SocialNetwork.dto.user.UserDTO;
import com.example.SocialNetwork.entity.group.GroupRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupRequestDTO {
    Long id;
    LocalDateTime time;
    UserDTO requestor;

    public GroupRequestDTO(GroupRequest groupRequest) {
        this.id = groupRequest.getId();
        this.requestor = new UserDTO(groupRequest.getRequestor());
        this.time = groupRequest.getTime();
    }
}
