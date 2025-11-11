package com.example.SocialNetwork.dto.post;

import com.example.SocialNetwork.entity.Image;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageDTO {
    Long id;
    String url;
    String filePath;

    public ImageDTO(Image image){
        this.id = image.getId();
        this.url = image.getUrl();
        this.filePath = image.getFilePath();
    }
}
