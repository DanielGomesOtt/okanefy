package com.okanefy.okanefy.repositories;

import com.okanefy.okanefy.models.Users;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<UserDetails> findByEmailAndStatus(String username, int status);

    Optional<UserDetails> findByEmailAndEmailNotAndStatus(String email, String email1, int status);
}
