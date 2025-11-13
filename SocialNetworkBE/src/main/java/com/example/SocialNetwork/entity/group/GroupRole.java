package com.example.SocialNetwork.entity.group;

import lombok.Getter;

@Getter
public enum GroupRole {
    OWNER(3),
    APPROVER(2),
    MEMBER(1);

    private final int level;

    GroupRole(int level) {
        this.level = level;
    }
}
