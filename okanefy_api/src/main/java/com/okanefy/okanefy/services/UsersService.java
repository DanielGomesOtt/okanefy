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
        Optional<Users> findUser = repository.findById(data.id());

        if(findUser.isPresent()) {
            String token = null;
            Users user = findUser.get();
            if(!data.name().isEmpty()) {
                user.setName(data.name());
            }

            if(!data.email().isEmpty() && !data.email().equals(user.getEmail())){
                Optional<UserDetails> alreadyExists = repository.findByEmailAndEmailNotAndStatus(
                        data.email(), user.getEmail() ,1
                );

                if (alreadyExists.isPresent()) {
                    throw new UserAlreadyExistsException("Esse e-mail já pertence a uma conta.");
                }

                user.setEmail(data.email());
                token = tokenService.signToken(user);
            }

            if(!data.password().isEmpty() && data.password().length() >= 8) {
                String encodedPassword = passwordEncoder.encode(data.password());

                user.setPassword(encodedPassword);
            }

            return new UsersDTO(user, token);
        }

        throw new UserNotFoundException("O usuário não foi encontrado.");
    }

    @Transactional
    public void deleteUser(Long id) {
        Optional<Users> result = repository.findById(id);
        result.ifPresent(user -> user.setStatus(0));
    }
}
