package com.deadperhaps.library.ui;

import com.deadperhaps.library.domain.Book;
import com.deadperhaps.library.domain.Role;

import java.util.List;
import java.util.Scanner;

public class TerminalView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayWelcomeMessage() {
        System.out.println("==========================================");
        System.out.println("  WITAJ W TERMINALOWYM MENEDZERZE KSIĄŻEK ");
        System.out.println("==========================================");
    }

    public void displayLoginMenu() {
        System.out.println("\n--- MENU LOGOWANIA ---");
        System.out.println("1. Zaloguj się");
        System.out.println("0. Wyjdź");
        System.out.print("Twój wybór: ");
    }

    public void displayLibraryMenu(Role currentRole) {
        System.out.println("\n--- MENU BIBLIOTEKI (" + currentRole + ") ---");
        System.out.println("1. Przeglądaj listę książek");
        System.out.println("2. Wyszukaj książkę (po autorze/tytule)");

        if (currentRole == Role.ADMIN) {
            System.out.println("3. Dodaj nową książkę");
            System.out.println("4. Edytuj istniejącą książkę");
            System.out.println("5. Usuń książkę");
        }

        System.out.println("6. Wyloguj się");
        System.out.println("0. Wyjdź z programu");
        System.out.print("Twój wybór: ");
    }

    public void displayBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("=> Brak książek do wyświetlenia.");
            return;
        }
        System.out.println("\n--- LISTA KSIĄŻEK ---");
        for (Book b : books) {
            System.out.println("ID: " + b.getId() + " | Tytuł: \"" + b.getTitle() + "\" | Autor: " + b.getAuthor());
        }
        System.out.println("---------------------");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public Long getLongInput(String prompt) {
        System.out.print(prompt);
        return Long.parseLong(scanner.nextLine());
    }
}