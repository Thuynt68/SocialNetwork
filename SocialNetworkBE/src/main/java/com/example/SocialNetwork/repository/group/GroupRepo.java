package com.example.SocialNetwork.repository.group;

import com.example.SocialNetwork.entity.PostStatus;
import com.example.SocialNetwork.entity.group.Group;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends JpaRepository<Group, Long> {
    @Query("SELECT g FROM Group g " +
            "WHERE (:keyword = '' OR LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Group> search(String keyword, Pageable pageable);

    @Query("SELECT g FROM Group g WHERE g.createUserId = :requestorId " +
            "AND (:keyword = '' OR LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Group> searchMyGroups(Long requestorId, String keyword, Pageable pageable);
    @Override
    @Transactional
    @Modifying
    void deleteById(Long id);

    @Query("SELECT COUNT(gr) FROM GroupRequest gr WHERE gr.group = :group")
    Long getTotalRequest(Group group);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.group = :group AND p.approvalStatus = :approvalStatus")
    Long getTotalPendingPost(Group group, PostStatus approvalStatus);
}
