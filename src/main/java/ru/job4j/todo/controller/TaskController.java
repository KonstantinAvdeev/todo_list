package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public String getAll(Model model, @RequestParam(required = false) Boolean done) {
        if (done == null) {
            model.addAttribute("tasks", taskService.findAll());
        } else {
            model.addAttribute("tasks", taskService.findAllByDone(done));
        }
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, @SessionAttribute User user, Model model) {
        task.setUser(user);
        taskService.create(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("error", "Задача не найдена!");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @GetMapping("/update/{id}")
    public String getUpdate(@PathVariable int id, Model model) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("error", "Задача не найдена!");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, @SessionAttribute User user, Model model) {
        task.setUser(user);
        boolean isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("error", "Задача не найдена!");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/markDone/{id}")
    public String toDone(@PathVariable int id, Model model) {
        boolean result = taskService.makeDone(id);
        if (!result) {
            model.addAttribute("error", "Задача не найдена!");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        boolean isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("error", "Задача не существует!");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

}
