package com.polainam.introductoryt1.validation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CandidateValidator {
    public boolean isValidRole(List<String> roles, String role) {
        if (roles.contains(role)) {
            return true;
        }
        System.out.println("Выберите одну из представленных ролей");
        return false;
    }

    public boolean isValidName(String name) {
        if (name.matches("^[А-Яа-яЁё]+$")) {
            return true;
        }
        System.out.println("Используйте только кириллицу");
        return false;
    }

    public boolean isValidEmail(String email) {
        if (email.matches("^[\\w-]+-[\\w-]+№\\d+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$")) {
            return true;
        }
        System.out.println("Адрес эл. почты должен быть в формате: инициалы-фамилия-№теста@example.ru");
        return false;
    }
}
