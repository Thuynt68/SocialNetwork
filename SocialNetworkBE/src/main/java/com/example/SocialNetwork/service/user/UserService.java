package com.example.SocialNetwork.service.user;

import com.example.SocialNetwork.dto.user.ChangePasswordRequest;
import com.example.SocialNetwork.dto.user.ProfileUpdateRequest;
import com.example.SocialNetwork.dto.user.UserDTO;
import com.example.SocialNetwork.entity.EmailVerification;
import com.example.SocialNetwork.entity.Role;
import com.example.SocialNetwork.entity.User;
import com.example.SocialNetwork.exception.AppException;
import com.example.SocialNetwork.exception.ErrorCode;
import com.example.SocialNetwork.repository.user.EmailVerificationRepo;
import com.example.SocialNetwork.repository.user.UserRepo;
import com.example.SocialNetwork.service.EmailService;
import com.example.SocialNetwork.service.auth.RoleService;
import com.example.SocialNetwork.utils.PageableUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepo userRepo;
    PasswordEncoder passwordEncoder;
    RoleService roleService;
    EmailVerificationRepo emailVerificationRepo;
    EmailService emailService;

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public User getById(Long id) {
        return userRepo.findById(id).orElseThrow();
    }

    public List<User> getByIds(List<Long> ids){
        return userRepo.findAllById(ids);
    }

    public void createUser(UserDTO request) {
        if (userRepo.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);
        if (userRepo.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_USED);
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setFullName(user.getFirstName() + " " + user.getLastName());
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.getByName("USER"));
        user.setRoles(roles);
        EmailVerification emailVerification = EmailVerification.builder()
                .user(user).token(UUID.randomUUID().toString())
                .expiryTime(LocalDateTime.now().plusHours(1))
                .build();
        userRepo.save(user);
        emailVerificationRepo.save(emailVerification);
        emailService.sendHtmlEmail(request.getEmail(), user.getFullName(), emailVerification.getToken());
    }

    public User getCurrentUser() {
        return getByUsername(getLoginUsername());
    }

    public String getLoginUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public User getByUsername(String username) {
        return userRepo.findByUsernameAndActive(username.toLowerCase(), true);
    }

    public Boolean updateUser(ProfileUpdateRequest request) {
        User user = getCurrentUser();
        user.setAvatarUrl(request.getAvatarUrl());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setFullName(user.getFirstName() + " " + user.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        userRepo.save(user);
        return true;
    }

    public Map<String, String> changePassword(ChangePasswordRequest request) {
        Map<String, String> response = new HashMap<>();
        User user = getCurrentUser();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            response.put("status", "error");
            response.put("message", "Mật khẩu hiện tại không chính xác");
        }
        else {
            response.put("status", "success");
            response.put("message", "Thay đổi mật khẩu thành công");
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepo.save(user);
        }
        return response;
    }

    public Page<UserDTO> search(String keyword, int page, int size){
        User requestor = getCurrentUser();
        Pageable pageable = PageableUtils.createPageable(page, size, "lastName");
        keyword = "%" + keyword + "%";
        Page<User> resultPage = userRepo.search(requestor, keyword, pageable);
        return resultPage.map(UserDTO::new);
    }

    public Boolean verifyEmail(String token) {
        EmailVerification emailVerification = emailVerificationRepo.findByToken(token);
        if (emailVerification != null && !emailVerification.isExpired()){
            User newUser = emailVerification.getUser();
            newUser.setActive(true);
            userRepo.save(newUser);
            emailVerificationRepo.delete(emailVerification);
            return true;
        }
        return false;
    }


}
