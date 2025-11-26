package com.example.SocialNetwork.controller.group;

import com.example.SocialNetwork.dto.ApiResponse;
import com.example.SocialNetwork.dto.group.GroupDTO;
import com.example.SocialNetwork.dto.group.UserGroupContext;
import com.example.SocialNetwork.dto.user.UserDTO;
import com.example.SocialNetwork.entity.group.Group;
import com.example.SocialNetwork.service.group.GroupService;
import com.example.SocialNetwork.service.group.UserGroupContextService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/group")
public class GroupController {
    GroupService groupService;
    UserGroupContextService userGroupContextService;

    @GetMapping
    public ApiResponse<List<Group>> search(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam int page, @RequestParam int size) {
        Page<Group> groupPage = groupService.search(keyword, page, size);
        return ApiResponse.<List<Group>>builder()
                .result(groupPage.getContent())
                .totalPages(groupPage.getTotalPages())
                .build();
    }

    @GetMapping("/my")
    public ApiResponse<List<Group>> getMyGroup(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam int page, @RequestParam int size) {
        Page<Group> groupPage = groupService.searchMyGroups(keyword, page, size);
        return ApiResponse.<List<Group>>builder()
                .result(groupPage.getContent())
                .totalPages(groupPage.getTotalPages())
                .build();
    }

    @GetMapping("/detail/{id}")
    public ApiResponse<GroupDTO> getGroupById(@PathVariable Long id) {
        return ApiResponse.<GroupDTO>builder()
                .result(groupService.getGroupResponse(id))
                .build();
    }

    @GetMapping("/context/{id}")
    public ApiResponse<UserGroupContext> getUserGroupContext(@PathVariable Long id) {
        return ApiResponse.<UserGroupContext>builder()
                .result(userGroupContextService.getUserGroupContext(id))
                .build();
    }

    @GetMapping("/members/{groupId}")
    public ApiResponse<List<UserDTO>> getMembersByGroupId(@PathVariable Long groupId) {
        return ApiResponse.<List<UserDTO>>builder()
                .result(groupService.getMembersByGroupId(groupId))
                .build();
    }

    @PostMapping("/add-member")
    public ApiResponse<Boolean> addMember(@RequestParam Long groupId, @RequestParam Long userId) {
        return ApiResponse.<Boolean>builder()
                .result(groupService.addGroupMember(groupId, userId))
                .build();
    }

    @DeleteMapping("/remove-member")
    public ApiResponse<Boolean> removeMember(@RequestParam Long groupId, @RequestParam Long userId) {
        return ApiResponse.<Boolean>builder()
                .result(groupService.removeGroupMember(groupId, userId))
                .build();
    }

    @DeleteMapping("/leave-group")
    public ApiResponse<Boolean> leaveGroup(@RequestParam Long groupId) {
        return ApiResponse.<Boolean>builder()
                .result(groupService.leaveGroup(groupId))
                .build();
    }

    @PostMapping
    public ApiResponse<Group> createGroup(@RequestBody GroupDTO request) {
        return ApiResponse.<Group>builder()
                .result(groupService.createGroup(request))
                .build();
    }

    @PutMapping
    public ApiResponse<Group> updateGroup(@RequestBody GroupDTO request) {
        return ApiResponse.<Group>builder()
                .result(groupService.updateGroup(request))
                .build();
    }

    @PutMapping("/change-role")
    public ApiResponse<Boolean> changeRole(
            @RequestParam Long groupId, @RequestParam Long userId, @RequestParam String newRole) {
        return ApiResponse.<Boolean>builder()
                .result(groupService.changeMemberRole(groupId, userId, newRole))
                .build();
    }

    @PutMapping("/change-owner")
    public ApiResponse<Boolean> changeOwner(@RequestParam Long groupId, @RequestParam Long userId) {
        return ApiResponse.<Boolean>builder()
                .result(groupService.changeOwner(groupId, userId))
                .build();
    }

    @DeleteMapping("/dissolve/{groupId}")
    public ApiResponse<Boolean> dissolveGroup(@PathVariable Long groupId) {
        return ApiResponse.<Boolean>builder()
                .result(groupService.dissolveGroup(groupId))
                .build();
    }
}
