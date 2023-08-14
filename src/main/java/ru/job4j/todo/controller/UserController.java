package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.SimpleUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final SimpleUserService userService;

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        List<TimeZone> zones = userService.getTimeZones();
        model.addAttribute("timeZones", zones);
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model, @RequestParam("timezone")
    String timeZone) {
        if (timeZone != null) {
            user.setTimezone(timeZone);
        } else {
            user.setTimezone("UTC");
        }
        Optional<User> userOptional = userService.save(user);
        user.setName("Гость");
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Не удалось зарегистрироваться!"
                    + " Возможно, данный логин занят. Попробуйте ещё раз");
            return "errors/404";
        }
        return "users/login";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        Optional<User> userOptional = userService.findByLoginAndPassword(user.getLogin(),
                user.getPassword());
        user.setName("Гость");
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Логин или пароль введены неверно");
            return "users/login";
        }
        var session = request.getSession();
        session.setAttribute("user", userOptional.get());
        return "redirect:/tasks";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

}