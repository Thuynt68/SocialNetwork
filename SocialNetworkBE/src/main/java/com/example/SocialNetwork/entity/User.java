package com.example.SocialNetwork.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "res_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String firstName;
    String lastName;
    String fullName;
    String username;
    String password;
    String email;
    String avatarUrl;
    LocalDate dateOfBirth;
    Boolean active;

    @ManyToMany
    List<Role> roles;


}
