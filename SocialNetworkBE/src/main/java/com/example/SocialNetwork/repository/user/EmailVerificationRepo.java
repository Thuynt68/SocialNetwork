package com.example.SocialNetwork.repository.user;

import com.example.SocialNetwork.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepo extends JpaRepository<EmailVerification, Long> {
    EmailVerification findByToken(String token);
}
