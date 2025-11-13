package com.example.SocialNetwork.repository.group;


import com.example.SocialNetwork.entity.User;
import com.example.SocialNetwork.entity.group.Group;
import com.example.SocialNetwork.entity.group.GroupMember;
import com.example.SocialNetwork.entity.group.GroupMemberId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepo extends JpaRepository<GroupMember, GroupMemberId> {
    @Transactional
    @Modifying
    void deleteByGroupAndMember(Group group, User member);
    GroupMember findByGroupAndMember(Group group, User user);
    List<GroupMember> findByGroup(Group group);
    boolean existsByGroupAndMember(Group group, User member);

    @Transactional
    @Modifying
    void deleteByGroup(Group group);

    @Query("SELECT COUNT (m) FROM GroupMember m WHERE m.group = :group")
    Long getTotalMember(Group group);
}
