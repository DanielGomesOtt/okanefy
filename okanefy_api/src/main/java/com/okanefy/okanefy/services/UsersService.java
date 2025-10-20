package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.users.UsersDTO;
import com.okanefy.okanefy.exceptions.UserAlreadyExistsException;
import com.okanefy.okanefy.exceptions.UserNotFoundException;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public UsersDTO updateUser(UsersDTO data) {
        Optional<Users> user = repository.findById(data.id());

        if(user.isPresent()) {
            Optional<UserDetails> alreadyExists = repository.findByEmailAndEmailNotAndStatus(
                    data.email(), user.get().getEmail() ,1
            );

            if (alreadyExists.isPresent()) {
                throw new UserAlreadyExistsException("Esse e-mail já pertence a uma conta.");
            }

            String encodedPassword = passwordEncoder.encode(data.password());

            user.get().setName(data.name());
            user.get().setEmail(data.email());
            user.get().setPassword(encodedPassword);

            String token = tokenService.signToken(user.get());

            return new UsersDTO(user.get(), token);
        }

        throw new UserNotFoundException("O usuário não foi encontrado.");
    }

    @Transactional
    public void deleteUser(Long id) {
        Optional<Users> result = repository.findById(id);
        result.ifPresent(user -> user.setStatus(0));
    }
}
