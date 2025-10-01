package ru.tikh1y.tacos.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.tikh1y.tacos.models.TacoOrder;
import ru.tikh1y.tacos.models.User;
import ru.tikh1y.tacos.repositories.OrderRepository;

@Controller
@RequestMapping("/orders")
@Slf4j
@SessionAttributes("tacoOrder")
public class OrderController {
    private final OrderRepository repository;

    public OrderController(OrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/current")
    public String orderForm(@ModelAttribute TacoOrder tacoOrder, Model model,
                            @AuthenticationPrincipal User user) {
        // Если заказа нет, создайте новый объект
        if (tacoOrder == null) {
            tacoOrder = new TacoOrder(); // создаем новый объект заказа
            log.warn("заказ тако отсутствует, создается новый заказ");
        }


        if (user != null) {
            tacoOrder.setDeliveryName(user.getUsername());
            tacoOrder.setDeliveryStreet(user.getStreet());
            tacoOrder.setDeliveryCity(user.getCity());
            tacoOrder.setDeliveryState(user.getState());
            tacoOrder.setDeliveryZip(user.getZip());
        }


        // Заполнение данных для демонстрации (можно убрать или изменить)
        tacoOrder.setCcCVV("123");
        tacoOrder.setCcExpiration("12/21");
        tacoOrder.setCcNumber("4000001234567899");

        // Добавляем объект заказа в модель
        model.addAttribute("tacoOrder", tacoOrder);

        return "orderForm";  // возвращаем название шаблона страницы
    }


    @PostMapping
    public String processOrder(@Valid TacoOrder tacoOrder, Errors errors,
                               SessionStatus sessionStatus,
                               @AuthenticationPrincipal User user,
                               RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "orderForm";
        }

        log.info(user.toString() + "\n\n" + user.getId());

        tacoOrder.setUserId(user.getId());

        log.info("Отправленный заказ: " + tacoOrder.toString());
        repository.save(tacoOrder);
        sessionStatus.setComplete();

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < tacoOrder.getTacos().size(); i++) {
            s.append(tacoOrder.getTacos().get(i).getName()).append("\n");
        }

        // Добавление сообщения об успешной регистрации
        redirectAttributes.addFlashAttribute("successMessage", "Заказ создан:\n" + s.toString());

        return "redirect:/home";
    }
}
