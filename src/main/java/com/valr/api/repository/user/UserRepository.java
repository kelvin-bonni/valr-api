package com.valr.api.repository.user;

import com.valr.api.model.user.Role;
import com.valr.api.model.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class UserRepository {

    private final PasswordEncoder encoder;

    private final Set<User> users = new HashSet<>();

    public UserRepository(PasswordEncoder encoder) {
        this.encoder = encoder;

        users.add(new User(UUID.randomUUID(), "john.doe@gmail.com", encoder.encode("pass123"), Role.USER));
        users.add(new User(UUID.randomUUID(), "jane.doe@gmail.com", encoder.encode("pass123"), Role.ADMIN));
    }

    public boolean save(User user) {
        User updated = new User(
                user.getId(),
                user.getEmail(),
                encoder.encode(user.getPassword()),
                user.getRole()
        );
        return users.add(updated);
    }

    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public Set<User> findAll() {
        return users;
    }

    public Optional<User> findByUUID(UUID uuid) {
        return users.stream()
                .filter(user -> user.getId().equals(uuid))
                .findFirst();
    }

    public boolean deleteByUUID(UUID uuid) {
        Optional<User> foundUser = findByUUID(uuid);

        return foundUser.map(user -> users.removeIf(u -> u.getId().equals(uuid))).orElse(false);
    }
}
