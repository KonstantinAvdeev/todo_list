package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.TaskRepository;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {
    private final TaskRepository taskRepository;
    private final CategoryService categoryService;

    @Override
    public Task create(Task task, List<Integer> categoryId) {
        var categories = categoryService.findById(categoryId);
        task.setCategories(categories);
        return taskRepository.create(task);
    }

    @Override
    public boolean update(Task task, List<Integer> categoryId) {
        var categories = categoryService.findById(categoryId);
        task.setCategories(categories);
        return taskRepository.update(task);
    }

    @Override
    public boolean deleteById(int taskId) {
        return taskRepository.deleteById(taskId);
    }

    @Override
    public boolean makeDone(int taskId) {
        return taskRepository.makeDone(taskId);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findAllByDone(boolean isDone) {
        return taskRepository.findAllByDone(isDone);
    }

    @Override
    public Optional<Task> findById(int taskId) {
        return taskRepository.findById(taskId);
    }

    public static void addUserTimeZone(User user, Task task) {
        var defaultZone = TimeZone.getDefault().toZoneId();
        var userZone = user.getTimezone() == null ? defaultZone : ZoneId.of(user.getTimezone());
        var time = task.getCreated()
                .atZone(defaultZone)
                .withZoneSameInstant(userZone)
                .toLocalDateTime();
        task.setCreated(time);
    }
}