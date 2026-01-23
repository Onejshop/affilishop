package com.obed.affilishop.repository;

import com.obed.affilishop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔍 Trouver un utilisateur par username (login)
    Optional<User> findByUsername(String username);

    // ❌ Vérifier si le username existe déjà (inscription)
    boolean existsByUsername(String username);

    // 🔍 Trouver par email et vérifier existence par email
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
