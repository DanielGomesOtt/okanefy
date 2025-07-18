package com.okanefy.okanefy.infra.security;

import com.okanefy.okanefy.repositories.UsersRepository;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsersRepository repository;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String authorization = this.recoverToken(request);

        if(authorization != null) {
            String subject = tokenService.verifyToken(authorization);
            Optional<UserDetails> user = repository.findByEmail(subject);

            if(user.isPresent()) {
                var authentication = new UsernamePasswordAuthenticationToken(user.get(), null, user.get().getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken (HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if(authorization != null) {
            return authorization.replace("Bearer: ", "");
        }

        return null;
    }
}
