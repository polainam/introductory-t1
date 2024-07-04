package com.polainam.introductoryt1.services;

import org.springframework.stereotype.Service;

import java.util.Scanner;
import java.util.function.Predicate;

@Service
public class InputValidationService {
    public String promptUserForValidInput(Scanner scanner, String prompt, Predicate<String> validator) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
        } while (!validator.test(input));
        return input;
    }
}
