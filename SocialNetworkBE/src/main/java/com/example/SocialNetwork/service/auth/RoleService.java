package com.example.SocialNetwork.service.auth;

import com.example.SocialNetwork.entity.Role;
import com.example.SocialNetwork.repository.RoleRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepo roleRepo;

    public Role getByName(String name){
        return roleRepo.findByName(name);
    }
}
