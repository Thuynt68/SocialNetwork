package com.example.SocialNetwork.service.group;

import com.example.SocialNetwork.dto.group.UserGroupContext;
import com.example.SocialNetwork.entity.User;
import com.example.SocialNetwork.entity.group.Group;
import com.example.SocialNetwork.entity.group.GroupMember;
import com.example.SocialNetwork.entity.group.GroupRole;
import com.example.SocialNetwork.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserGroupContextService {
    GroupService groupService;
    UserService userService;
    GroupMemberService groupMemberService;
    GroupRequestService groupRequestService;
    public UserGroupContext getUserGroupContext(Long groupId) {
        Group group = groupService.getById(groupId);
        User requestor = userService.getCurrentUser();
        GroupMember groupMember = groupMemberService.getByGroupAndMember(group, requestor);
        boolean owner, requestSent, approver, member, joined;
        joined = groupMember != null;
        if (joined){
            owner = groupMember.getRole().equals(GroupRole.OWNER);
            approver = groupMember.getRole().equals(GroupRole.APPROVER);
            member = !owner && !approver;
        }
        else {
            owner = approver = member = false;
        }
        requestSent = groupRequestService.existsRequest(group, requestor);
        return UserGroupContext.builder()
                .owner(owner).approver(approver).member(member).joined(joined)
                .requestSent(requestSent)
                .build();
    }
}
