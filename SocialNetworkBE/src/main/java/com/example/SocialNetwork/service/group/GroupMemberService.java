package com.example.SocialNetwork.service.group;

import com.example.SocialNetwork.dto.user.UserDTO;
import com.example.SocialNetwork.entity.User;
import com.example.SocialNetwork.entity.group.Group;
import com.example.SocialNetwork.entity.group.GroupMember;
import com.example.SocialNetwork.entity.group.GroupRole;
import com.example.SocialNetwork.repository.group.GroupMemberRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupMemberService {
    GroupMemberRepo groupMemberRepo;

    public boolean addGroupMember(Group group, User user, String role) {
        GroupMember groupMember = GroupMember.builder()
                .group(group).member(user).role(GroupRole.valueOf(role))
                .build();
        groupMemberRepo.save(groupMember);
        return true;
    }

    public boolean removeGroupMember(Group group, User member) {
        groupMemberRepo.deleteByGroupAndMember(group, member);
        return true;
    }

    public boolean existsByGroupAndMember(Group group, User member) {
        return groupMemberRepo.existsByGroupAndMember(group, member);
    }

    public List<UserDTO> getByGroup(Group group) {
        List<GroupMember> groupMembers = groupMemberRepo.findByGroup(group);
        return groupMembers.stream().map(UserDTO::new).sorted().toList();
    }

    public GroupMember getByGroupAndMember(Group group, User user){
        return groupMemberRepo.findByGroupAndMember(group, user);
    }

    public boolean changeMemberRole(Group group, User user, String newRole) {
        GroupMember groupMember = getByGroupAndMember(group, user);
        groupMember.setRole(GroupRole.valueOf(newRole));
        groupMemberRepo.save(groupMember);
        return true;
    }

    public void removeAllGroupMembers(Group group) {
        groupMemberRepo.deleteByGroup(group);
    }

    public Long getTotalMember(Group group) {
        return groupMemberRepo.getTotalMember(group);
    }
}
