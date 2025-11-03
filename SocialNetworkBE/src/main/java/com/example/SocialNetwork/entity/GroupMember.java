package com.example.SocialNetwork.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@IdClass(GroupMemberId.class)
public class GroupMember {
    @Id
    @ManyToOne
    Group group;

    @Id
    @ManyToOne
    User member;

    @Enumerated
    GroupRole role;
}

