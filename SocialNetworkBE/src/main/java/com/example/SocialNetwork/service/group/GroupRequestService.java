package com.example.SocialNetwork.service.group;


import com.example.SocialNetwork.dto.group.GroupRequestDTO;
import com.example.SocialNetwork.entity.User;
import com.example.SocialNetwork.entity.group.Group;
import com.example.SocialNetwork.entity.group.GroupRequest;
import com.example.SocialNetwork.repository.group.GroupRequestRepo;
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
public class GroupRequestService {
    GroupRequestRepo groupRequestRepo;
    UserService userService;
    GroupService groupService;
    NotificationService notificationService;

    public GroupRequest createGroupRequest(Long groupId) {
        Group group = groupService.getById(groupId);
        User requestor = userService.getCurrentUser();
        User groupOwner = userService.getById(group.getCreateUserId());
        GroupRequest request = GroupRequest.builder()
                .group(group).requestor(requestor)
                .time(LocalDateTime.now())
                .build();
        request = groupRequestRepo.save(request);
        notificationService.notifyGroupRequest(requestor, groupOwner, group);
        return request;
    }

    public void actionRequestById(Long requestId, boolean accept) {
        GroupRequest request = getById(requestId);
        User receiver = request.getRequestor();
        if (accept)
            groupService.addGroupMember(request.getGroup().getId(), receiver.getId());
        notificationService.notifyActionGroupRequest(receiver, request.getGroup(), accept);
        groupRequestRepo.delete(request);
    }

    public GroupRequest getById(Long requestId) {
        return groupRequestRepo.findById(requestId).orElseThrow();
    }

    public List<GroupRequestDTO> getRequests(Long groupId) {
        Group group = groupService.getById(groupId);
        List<GroupRequest> groupRequests = groupRequestRepo.findByGroup(group);
        return groupRequests.stream().map(GroupRequestDTO::new).toList();
    }

    public boolean existsRequest(Group group, User requestor) {
        return groupRequestRepo.existsByGroupAndRequestor(group, requestor);
    }

}
