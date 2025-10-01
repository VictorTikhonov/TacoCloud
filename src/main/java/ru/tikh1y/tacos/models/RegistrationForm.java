package ru.tikh1y.tacos.models;


import lombok.Data;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationForm {
    @NotNull
    @Size(min = 3, max = 30, message = "Имя должно содержать от 3 до 30 символов")
    private String username;
    @NotNull
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    private String confirm;

    @NotBlank(message = "Полное имя не может быть пустым")
    @Size(max = 30, message = "Полное имя должно содержать не более 30 символов")
    private String fullname;


    @Pattern(
            regexp = "^[а-яА-ЯёЁa-zA-Z0-9\\s,.-]+(,\\s*д\\.\\s*\\d+)(,\\s*кв\\.\\s*\\d+)?$",
            message = "Улица и дом обязательны, пишите, согласно примеру"
    )
    @Size(max = 50, message = "Адрес должен содержать не более 50 символов")
    private String street;

    @NotBlank(message = "Название города не может быть пустым")
    @Size(max = 30, message = "Название города должно содержать не более 30 символов")
    private String city;

    @NotBlank(message = "Название области не может быть пустым")
    @Size(max = 30, message = "Название области должно содержать не более 30 символов")
    private String state;

    @NotNull
    @Pattern(regexp = "\\d{6}", message = "Почтовый индекс должен содержать ровно 6 цифр.")
    private String zip;

    @NotNull
    @Pattern(regexp = "8\\d{10}", message = "Номер должен начинаться с '8' и содержать 11 цифр")
    private String phone;

    public User toUser(PasswordEncoder passwordEncoder)
    {
        return new User(username, passwordEncoder.encode(password),
                fullname, street, city, state, zip, phone);
    }



    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirm);
    }
}
