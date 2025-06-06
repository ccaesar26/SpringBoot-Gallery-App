package com.example.rest_api.users.database.repository;

import com.example.rest_api.users.database.model.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //@Query(value = "SELECT user FROM UserEntity user WHERE user.username=:username")
    //@Query(value = "SELECT * FROM app_user WHERE username=:username", nativeQuery = true)
    List<UserEntity> findAllByUsername(String username);

    @Modifying
    @Query(value = "update users set username=:username, email=:email, password=:password WHERE id=:id", nativeQuery = true)
    void updateUserEntity(Long id, String username, String email, String password);

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    UserEntity findByIdWithRoles(@Param("id") Long id);
}
