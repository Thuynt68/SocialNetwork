package com.example.SocialNetwork.repository.post;

import com.example.SocialNetwork.entity.Comment;
import com.example.SocialNetwork.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    @Transactional
    @Modifying
    void deleteByPost(Post post);
}
