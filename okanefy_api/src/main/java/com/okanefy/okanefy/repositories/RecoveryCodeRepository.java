package com.okanefy.okanefy.repositories;

import com.okanefy.okanefy.models.RecoveryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecoveryCodeRepository extends JpaRepository<RecoveryCode, Long> {

    @Query(value= "SELECT * FROM recovery_code WHERE email = :email AND code = :code AND expiration_date >= now()", nativeQuery = true)
    Optional<RecoveryCode> findValidCodeByEmail(@Param("email") String email, @Param("code") String code);

}
