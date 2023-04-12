package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmUserRepository implements UserRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        Optional<User> userOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(user));
            userOptional = Optional.of(user);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return userOptional;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional("FROM User WHERE login = :login "
                        + "AND password = :password", User.class,
                Map.of("login", login, "password", password));
    }

    @Override
    public Optional<User> findById(int userId) {
        return crudRepository.optional("FROM User WHERE id = :id", User.class,
                Map.of("id", userId));
    }

}