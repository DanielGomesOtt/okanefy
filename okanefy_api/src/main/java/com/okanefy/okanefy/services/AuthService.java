package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.Auth.CreatedUserDTO;
import com.okanefy.okanefy.dto.Auth.RegisterUserDTO;
import com.okanefy.okanefy.infra.security.TokenService;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }


    @Transactional
    public CreatedUserDTO save (RegisterUserDTO user) {
        String passwordEncoded = passwordEncoder.encode(user.password());

        Users newUser = new Users(user);
        newUser.setPassword(passwordEncoded);
        newUser.setStatus(1);
        Users createdUser = repository.save(newUser);

        String token = tokenService.signToken(createdUser);

        return new CreatedUserDTO(createdUser.getId(), createdUser.getName(), createdUser.getEmail(), token);
    }
}
