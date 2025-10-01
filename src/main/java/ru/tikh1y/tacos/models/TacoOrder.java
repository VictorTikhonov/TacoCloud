package ru.tikh1y.tacos.models;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TacoOrder implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Date placeAt;

    @NotBlank(message = "Имя для доставки обязательно")
    @Size(max = 30, message = "Имя должно содержать не более 30 символов")
    private String deliveryName;


    @Pattern(
            regexp = "^[а-яА-ЯёЁa-zA-Z0-9\\s,.-]+(,\\s*д\\.\\s*\\d+)(,\\s*кв\\.\\s*\\d+)?$",
            message = "Улица и дом обязательны, пишите, согласно примеру"
    )
    @Size(max = 50, message = "Адрес должен содержать не более 50 символов")
    private String deliveryStreet;

    @NotBlank(message = "Название города не может быть пустым")
    @Size(max = 30, message = "Название города должно содержать не более 30 символов")
    private String deliveryCity;

    @NotBlank(message = "Название области не может быть пустым")
    @Size(max = 30, message = "Название области должно содержать не более 30 символов")
    private String deliveryState;

    @NotNull
    @Pattern(regexp = "\\d{6}", message = "Почтовый индекс должен содержать ровно 6 цифр.")
    private String deliveryZip;

    @CreditCardNumber(message = "Номер кредитной карты неверный")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message = "Формат должен быть MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Неверный CVV")
    private String ccCVV;

    private Long userId;

    private List<Taco> tacos = new ArrayList<>();

    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }
}
