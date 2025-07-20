package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.Auth.CreatedUserDTO;
import com.okanefy.okanefy.dto.Auth.RegisterUserDTO;
import com.okanefy.okanefy.exceptions.UserAlreadyExistsException;
import com.okanefy.okanefy.infra.security.TokenService;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


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
        return repository.findByEmailAndStatus(username, 1)
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));
    }


    @Transactional
    public CreatedUserDTO save (RegisterUserDTO user) {
        Optional<UserDetails> alreadyExists = repository.findByEmailAndStatus(user.email(), 1);

        if (alreadyExists.isPresent()) {
            throw new UserAlreadyExistsException("Esse e-mail já pertence a uma conta.");
        }

        String passwordEncoded = passwordEncoder.encode(user.password());

        Users newUser = new Users(user);
        newUser.setPassword(passwordEncoded);
        newUser.setStatus(1);
        Users createdUser = repository.save(newUser);

        String token = tokenService.signToken(createdUser);

        return new CreatedUserDTO(createdUser.getId(), createdUser.getName(), createdUser.getEmail(), token);
    }
}
