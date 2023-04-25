package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final SimpleTaskService taskService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;
    private final SimpleUserService userService;

    @GetMapping
    public String getAll(Model model, @RequestParam(required = false) Boolean done,
                         HttpSession httpSession) {
        var user = (User) httpSession.getAttribute("user");
        List<Task> tasks;
        if (done == null) {
            tasks = taskService.findAll();
            tasks.forEach(task -> taskService.addUserTimeZone(user, task));
            model.addAttribute("tasks", tasks);
        } else {
            tasks = taskService.findAllByDone(done);
            tasks.forEach(task -> taskService.addUserTimeZone(user, task));
            model.addAttribute("tasks", tasks);
        }
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("categoryId", new ArrayList<Integer>());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, @SessionAttribute User user, Model model,
                         @RequestParam("categoryId") List<Integer> categoryId) {
        task.setUser(user);
        taskService.create(task, categoryId);
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
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("categoryId", new ArrayList<Integer>());
        return "tasks/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, @SessionAttribute User user, Model model,
                         @RequestParam("categoryId") List<Integer> categoryId) {
        task.setUser(user);
        boolean isUpdated = taskService.update(task, categoryId);
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
