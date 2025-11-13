package com.example.SocialNetwork.dto.user;

import com.example.SocialNetwork.entity.User;
import com.example.SocialNetwork.entity.group.GroupMember;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Comparable<UserDTO>{

    Long id;

    @NotBlank(message = "Họ và tên đệm là bắt buộc")
    String firstName;

    @NotBlank(message = "Tên là bắt buộc")
    String lastName;

    String fullName;

    @NotBlank(message = "Tên đăng nhập là bắt buộc")
    @Size(min = 3, message = "Tên đăng nhập phải có ít nhất 3 ký tự")
    String username;

    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Email không hợp lệ")
    String email;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    @Size(min = 3, message = "Mật khẩu phải có ít nhất 3 ký tự")
    String password;

    @NotNull(message = "Ngày sinh là bắt buộc")
    LocalDate dateOfBirth;

    String avatarUrl;

    String groupRole;

    String relation;


    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.avatarUrl = user.getAvatarUrl();
        this.dateOfBirth = user.getDateOfBirth();
    }

    public UserDTO(GroupMember member) {
        User user = member.getMember();
        this.id = user.getId();
        this.username = user.getUsername();
        this.lastName = user.getLastName();
        this.fullName = user.getFullName();
        this.avatarUrl = user.getAvatarUrl();
        this.groupRole = member.getRole().toString();
    }
    @Override
    public int compareTo(UserDTO o) {
        return this.lastName.compareTo(o.lastName);
    }
}
