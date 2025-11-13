package com.example.SocialNetwork.dto;

import com.example.SocialNetwork.entity.Image;
import com.example.SocialNetwork.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class MessageCustom implements Comparable<MessageCustom>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String content;


    @Transient
    List<Image> imageList;

    @ManyToOne
    User sender;
    LocalDateTime time;
    Boolean isRead;

    @Override
    public int compareTo(@NotNull MessageCustom m) {
        return this.getTime().compareTo(m.getTime());
    }
}
