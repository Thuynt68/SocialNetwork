package com.example.SocialNetwork.repository.post;

import com.example.SocialNetwork.entity.Post;
import com.example.SocialNetwork.entity.PostStatus;
import com.example.SocialNetwork.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.author = :author")
    List<Post> findByAuthor(User author);

    @Query("SELECT p FROM Post p WHERE p.approvalStatus IS NULL OR p.approvalStatus = :approvedStatus")
    List<Post> findAllPost(PostStatus approvedStatus);

}
