package ru.tikh1y.tacos.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tikh1y.tacos.models.TacoOrder;
import ru.tikh1y.tacos.models.User;
import ru.tikh1y.tacos.repositories.JdbcUserRepository;
import ru.tikh1y.tacos.repositories.OrderRepository;
import ru.tikh1y.tacos.repositories.TacoRepository;

import java.util.List;

@Controller
public class HomeController {

    private final OrderRepository orderRepository;

    public HomeController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "username", required = false) String username) {
        // Добавляем в модель введенный логин
        if (username != null) {
            model.addAttribute("username", username);
        }

        if (error != null) {
            model.addAttribute("error", "Неверное имя пользователя или пароль. Попробуйте снова.");
        }
        return "login";
    }

    @GetMapping("/home/myOrders")
    public String getOrders(Model model, @AuthenticationPrincipal User user) {

        List<TacoOrder> orders = orderRepository.findOrdersByUserId(user.getId());

        // Проверка, что заказы существуют
        if (orders.isEmpty()) {
            // Можно добавить атрибут для отображения сообщения о пустом списке
            model.addAttribute("message", "У вас нет заказов.");
        }

        // Добавляем заказы в модель
        model.addAttribute("orders", orders);
        return "orders";
    }
}




