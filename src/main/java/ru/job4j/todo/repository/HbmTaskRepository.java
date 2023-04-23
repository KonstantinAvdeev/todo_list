package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmTaskRepository implements TaskRepository {

    private final CrudRepository crudRepository;

    @Override
    public Task create(Task task) {
        crudRepository.run(session -> session.persist(task));
        return task;
    }

    @Override
    public boolean update(Task task) {
        boolean result = false;
        try {
            crudRepository.run(session -> session.merge(task));
            result = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean deleteById(int taskId) {
        boolean result = false;
        try {
            crudRepository.run(
                    "DELETE FROM Task WHERE id = :id",
                    Map.of("id", taskId)
            );
            result = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean makeDone(int taskId) {
        boolean result = false;
        try {
            crudRepository.run("UPDATE Task SET done = true WHERE id = :id",
                    Map.of("id", taskId));
            result = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Task> findAll() {
        return crudRepository.query("SELECT DISTINCT t FROM Task t JOIN FETCH t.categories "
                + "JOIN FETCH t.priority ORDER BY t.id", Task.class);
    }

    @Override
    public List<Task> findAllByDone(boolean isDone) {
        return crudRepository.query("SELECT DISTINCT t FROM Task t JOIN FETCH t.categories "
                        + "JOIN FETCH t.priority WHERE t.done = :done ORDER BY t.id",
                Task.class, Map.of("done", isDone));
    }

    @Override
    public Optional<Task> findById(int taskId) {
        return crudRepository.optional("FROM Task t JOIN FETCH t.categories JOIN FETCH "
                        + "t.priority WHERE t.id = :id ORDER BY t.id",
                Task.class, Map.of("id", taskId));
    }

}