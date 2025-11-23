package com.fahmi.chatappapi.repository;

import com.fahmi.chatappapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT nextval('username_seq')", nativeQuery = true)
    long getUsernameNumber();

    List<User> findByUsernameIsContainingIgnoreCase(String username);
}
