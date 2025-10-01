package ru.tikh1y.tacos.controllers;


import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.tikh1y.tacos.models.RegistrationForm;
import ru.tikh1y.tacos.repositories.UserRepository;



@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registration";
    }


    @PostMapping
    public String processRegistration(@Valid RegistrationForm form, Errors errors, Model model,
                                      RedirectAttributes redirectAttributes) {

        // Если есть ошибки в форме, возвращаем обратно
        if (errors.hasErrors() && !form.isPasswordConfirmed()) {
            model.addAttribute("registrationForm", form);  // Сохраняем данные формы, чтобы они не исчезли
            return "registration";
        }

        // Если есть ошибки в форме, возвращаем обратно
        if (errors.hasErrors()) {
            model.addAttribute("registrationForm", form);  // Сохраняем данные формы, чтобы они не исчезли
            return "registration";
        }

        // Если пароли не совпадают
        if (!form.isPasswordConfirmed()) {
            errors.rejectValue("confirm", "password.mismatch", "Пароли не совпадают");
            model.addAttribute("registrationForm", form);  // Сохраняем данные формы
            return "registration";
        }

        // Сохраняем нового пользователя в репозитории
        repository.save(form.toUser(this.passwordEncoder));

        // Добавление сообщения об успешной регистрации
        redirectAttributes.addFlashAttribute("successMessage", form.getFullname() + ", регистрация прошла успешно!");

        return "redirect:/login";
    }
}
