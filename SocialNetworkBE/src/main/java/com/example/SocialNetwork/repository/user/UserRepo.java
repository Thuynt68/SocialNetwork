package com.example.SocialNetwork.repository.user;

import com.example.SocialNetwork.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    User findByUsernameAndActive(String username, Boolean active);

    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(:keyword) AND u <> :requestor")
    Page<User> search(User requestor, String keyword, Pageable pageable);


}
