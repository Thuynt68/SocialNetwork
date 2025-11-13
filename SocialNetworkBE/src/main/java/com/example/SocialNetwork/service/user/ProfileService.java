package com.example.SocialNetwork.service.user;


import com.example.SocialNetwork.dto.user.UserDTO;
import com.example.SocialNetwork.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
//    UserService userService;
//    FriendshipService friendshipService;
//    FriendRequestService friendRequestService;
//
//    public UserDTO getProfileById(Long id) {
//        User requestor = userService.getCurrentUser();
//        User user = userService.getById(id);
//        UserDTO response = new UserDTO(user);
//        String relation;
//        if (requestor.getId().equals(user.getId())){
//            relation = "myProfile";
//        } else if (friendshipService.existsByUsers(requestor, user)) {
//            relation = "friend";
//        } else if (friendRequestService.getRequestByUsers(user, requestor) != null) {
//            relation = "hasRequest";
//        } else if (friendRequestService.getRequestByUsers(requestor, user) != null) {
//            relation = "sentRequest";
//        }
//        else relation = "toSendRequest";
//        response.setRelation(relation);
//        return response;
//    }

}
