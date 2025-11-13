package com.example.SocialNetwork.repository.image;

import com.example.SocialNetwork.entity.Image;
import com.example.SocialNetwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {
    List<Image> findByPost(Post post);


}
