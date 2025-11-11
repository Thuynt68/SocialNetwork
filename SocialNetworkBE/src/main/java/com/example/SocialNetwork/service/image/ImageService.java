package com.example.SocialNetwork.service.image;


import com.example.SocialNetwork.dto.post.ImageDTO;
import com.example.SocialNetwork.dto.post.PostDTO;
import com.example.SocialNetwork.entity.Image;
import com.example.SocialNetwork.entity.Post;
import com.example.SocialNetwork.repository.image.ImageRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageService {
    ImageRepo imageRepo;

    public void updatePostImages(PostDTO request, Post post) {
        List<Image> newImages = new ArrayList<>();
        for (ImageDTO i: request.getNewImages()){
            Image newImage = Image.builder()
                    .url(i.getUrl()).filePath(i.getFilePath()).post(post)
                    .build();
            newImages.add(newImage);
        }
        List<Long> deleteImageIds = request.getDeleteImages().stream()
                .map(ImageDTO::getId).toList();
        imageRepo.saveAll(newImages);
        imageRepo.deleteAllById(deleteImageIds);
    }

    public void deleteAllImagesByPostId(Post post) {
        List<Image> images = imageRepo.findByPost(post);
        imageRepo.deleteAll(images);
    }

    public void saveAll(List<Image> images) {
        imageRepo.saveAll(images);
    }
}
