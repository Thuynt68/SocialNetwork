package com.example.SocialNetwork.repository.group;

import com.example.SocialNetwork.entity.User;
import com.example.SocialNetwork.entity.group.Group;
import com.example.SocialNetwork.entity.group.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRequestRepo extends JpaRepository<GroupRequest, Long> {

    List<GroupRequest> findByGroup(Group group);

    boolean existsByGroupAndRequestor(Group group, User requestor);

}
