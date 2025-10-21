package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.auth.CreatedUserDTO;
import com.okanefy.okanefy.dto.auth.ForgotPasswordDTO;
import com.okanefy.okanefy.dto.auth.RegisterUserDTO;
import com.okanefy.okanefy.dto.auth.UpdatePasswordDTO;
import com.okanefy.okanefy.dto.email.EmailDTO;
import com.okanefy.okanefy.exceptions.UserAlreadyExistsException;
import com.okanefy.okanefy.models.RecoveryCode;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.RecoveryCodeRepository;
import com.okanefy.okanefy.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;


@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RecoveryCodeRepository recoveryCodeRepository;

    @Autowired
    private EmailService emailService;

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

    @Transactional
    public void createRecoveryCode(ForgotPasswordDTO data) {
        Optional<UserDetails> user = repository.findByEmailAndStatus(data.email(), 1);

        if (user.isPresent()) {
            RecoveryCode recoveryCode = new RecoveryCode();
            String nowFormatted = nowDateTimeFormatted();
            String code = generateRandomString();

            recoveryCode.setCode(code);
            recoveryCode.setExpirationDate(nowFormatted);
            recoveryCode.setEmail(data.email());
            recoveryCode.setUsed(0);
            recoveryCodeRepository.save(recoveryCode);

            sendRecoveryCodeEmail(data.email(), recoveryCode.getCode());
        }
    }

    @Transactional
    public HttpStatusCode confirmRecoveryCode(String email, String code) {
        Optional<RecoveryCode> recoveryCode = recoveryCodeRepository.findValidCodeByEmail(email, code);
        if(recoveryCode.isPresent()) {
            recoveryCode.get().setUsed(1);
            return HttpStatusCode.valueOf(200);
        }

        return HttpStatusCode.valueOf(404);
    }

    @Transactional
    public void updatePassword(UpdatePasswordDTO data) {
        Optional<UserDetails> user = repository.findByEmailAndStatus(data.email(), 1);

        if(user.isPresent()) {
            String newPasswordEncoded = passwordEncoder.encode(data.password());
            Users updatedUser = (Users) user.get();
            updatedUser.setPassword(newPasswordEncoded);
        } else {
            throw new UsernameNotFoundException("Usuário incorreto.");
        }
    }

    private static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 9; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }

    private static String nowDateTimeFormatted() {
        LocalDateTime now = LocalDateTime.now().plusMinutes(15L);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    private void sendRecoveryCodeEmail(String to, String recoveryCode) {
        EmailDTO email = new EmailDTO(
                to,
                "Código de recuperação de senha",
                "Aqui está seu código de recuperação: "
                + recoveryCode
        );

        emailService.sendEmail(email);
    }
}
