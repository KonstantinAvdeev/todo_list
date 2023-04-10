package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmTaskRepository implements TaskRepository {

    private final SessionFactory sf;

    @Override
    public Task create(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception exception) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public boolean update(Task task) {
        Session session = sf.openSession();
        boolean result = true;
        try {
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
        } catch (Exception exception) {
            result = false;
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean deleteById(int taskId) {
        Session session = sf.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            var query = session.createQuery("DELETE Task WHERE id = :id")
                    .setParameter("id", taskId);
            result = query.executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception exception) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean makeDone(int taskId) {
        Session session = sf.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            var query = session.createQuery(
                            "UPDATE Task SET done = :done WHERE id = :id")
                    .setParameter("done", true)
                    .setParameter("id", taskId);
            result = query.executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception exception) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Task> findAll() {
        Session session = sf.openSession();
        List<Task> tasks = new ArrayList<>();
        try {
            session.beginTransaction();
            tasks = session.createQuery("FROM Task", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception exception) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return tasks;
    }

    @Override
    public List<Task> findAllByDone(boolean isDone) {
        Session session = sf.openSession();
        List<Task> tasks = new ArrayList<>();
        try {
            session.beginTransaction();
            tasks = session.createQuery("FROM Task t WHERE done = :done", Task.class)
                    .setParameter("done", isDone).list();
            session.getTransaction().commit();
        } catch (Exception exception) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return tasks;
    }

    @Override
    public Optional<Task> findById(int taskId) {
        Session session = sf.openSession();
        Optional<Task> task = Optional.empty();
        try {
            session.beginTransaction();
            task = session.createQuery("FROM Task t WHERE id = :id", Task.class)
                    .setParameter("id", taskId).uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception exception) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return task;
    }

}
