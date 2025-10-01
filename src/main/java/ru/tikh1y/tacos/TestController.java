package ru.tikh1y.tacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Slf4j
@Controller
public class TestController {

    private static int i = 0;

    @ModelAttribute
    public void init(Model model)
    {
        log.info("Метод ModelAttribute " + ++i);
    }

}
