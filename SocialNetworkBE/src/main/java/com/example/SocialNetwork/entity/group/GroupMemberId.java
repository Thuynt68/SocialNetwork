package com.example.SocialNetwork.entity.group;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupMemberId implements Serializable {
    Long group;
    Long member;
}
