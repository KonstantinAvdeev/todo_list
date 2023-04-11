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
        try {
            crudRepository.run(session -> session.merge(task));
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(int taskId) {
        try {
            crudRepository.run(
                    "DELETE FROM Task WHERE id = :id",
                    Map.of("id", taskId)
            );
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean makeDone(int taskId) {
        try {
            crudRepository.run("UPDATE Task SET done = true WHERE id = :id",
                    Map.of("id", taskId));
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Task> findAll() {
        return crudRepository.query("FROM Task", Task.class);
    }

    @Override
    public List<Task> findAllByDone(boolean isDone) {
        return crudRepository.query("FROM Task WHERE done = :done",
                Task.class, Map.of("done", isDone));
    }

    @Override
    public Optional<Task> findById(int taskId) {
        return crudRepository.optional("FROM Task WHERE id = :id",
                Task.class, Map.of("id", taskId));
    }

}
