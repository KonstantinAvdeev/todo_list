package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmUserRepository implements UserRepository {
    private final SessionFactory sf;

    @Override
    public Optional<User> save(User user) {
        Session session = sf.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            result = Optional.of(user);
        } catch (Exception exception) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Session session = sf.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            var query = session.createQuery("FROM User WHERE login = :login "
                            + "AND password = :password", User.class).setParameter("login", login)
                    .setParameter("password", password);
            result = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception exception) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Optional<User> findById(int userId) {
        Session session = sf.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            var query = session.createQuery("FROM User WHERE id = :id", User.class)
                    .setParameter("id", userId);
            result = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception exception) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

}