package com.deadperhaps.library.ui;

import com.deadperhaps.library.domain.Book;
import com.deadperhaps.library.domain.Role;
import com.deadperhaps.library.repository.InMemoryBookRepository;
import com.deadperhaps.library.repository.InMemoryUserRepository;
import com.deadperhaps.library.service.AuthService;
import com.deadperhaps.library.service.LibraryService;

import java.util.List;
import java.util.Scanner;

public class TerminalApp {

    public static void main(String[] args) {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryBookRepository bookRepository = new InMemoryBookRepository();

        AuthService authService = new AuthService(userRepository);
        LibraryService libraryService = new LibraryService(bookRepository, authService);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("==========================================");
        System.out.println("  WITAJ W TERMINALOWYM MENEDZERZE KSIĄŻEK ");
        System.out.println("==========================================");

        while (running) {
            if (!authService.isLoggedIn()) {
                System.out.println("\n--- MENU LOGOWANIA ---");
                System.out.println("1. Zaloguj się");
                System.out.println("0. Wyjdź");
                System.out.print("Twój wybór: ");
                String choice = scanner.nextLine();

                if ("1".equals(choice)) {
                    System.out.print("Podaj login: ");
                    String username = scanner.nextLine();
                    System.out.print("Podaj hasło: ");
                    String password = scanner.nextLine();

                    if (authService.login(username, password)) {
                        System.out.println("=> Zalogowano pomyślnie jako: " + username);
                    } else {
                        System.out.println("=> Błędny login lub hasło!");
                    }
                } else if ("0".equals(choice)) {
                    running = false;
                    System.out.println("=> Zamykanie programu. Do widzenia!");
                } else {
                    System.out.println("=> Nieznana opcja.");
                }
            } else {
                Role currentRole = authService.getLoggedInUser().getRole();
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
                String choice = scanner.nextLine();

                try {
                    switch (choice) {
                        case "1":
                            printBooks(libraryService.getAllBooks());
                            break;
                        case "2":
                            System.out.print("Podaj fragment tytułu lub nazwisko autora: ");
                            String keyword = scanner.nextLine();
                            printBooks(libraryService.searchBooks(keyword));
                            break;
                        case "3":
                            if (currentRole != Role.ADMIN) { System.out.println("=> Brak uprawnień!"); break; }
                            System.out.print("Podaj tytuł: ");
                            String title = scanner.nextLine();
                            System.out.print("Podaj autora: ");
                            String author = scanner.nextLine();
                            libraryService.addBook(title, author);
                            System.out.println("=> Książka została dodana!");
                            break;
                        case "4":
                            if (currentRole != Role.ADMIN) { System.out.println("=> Brak uprawnień!"); break; }
                            System.out.print("Podaj ID książki do edycji: ");
                            Long editId = Long.parseLong(scanner.nextLine());
                            System.out.print("Podaj nowy tytuł: ");
                            String newTitle = scanner.nextLine();
                            System.out.print("Podaj nowego autora: ");
                            String newAuthor = scanner.nextLine();
                            libraryService.updateBook(editId, newTitle, newAuthor);
                            System.out.println("=> Książka została zaktualizowana!");
                            break;
                        case "5":
                            if (currentRole != Role.ADMIN) { System.out.println("=> Brak uprawnień!"); break; }
                            System.out.print("Podaj ID książki do usunięcia: ");
                            Long deleteId = Long.parseLong(scanner.nextLine());
                            System.out.println("Czy na pewno chcesz usunąć książkę o ID: " + deleteId + " ?");
                            System.out.println("1. Tak");
                            System.out.println("2. Nie");
                            System.out.print("Twój Wybór: ");
                            String confirm = scanner.nextLine();
                            if (confirm.equals("1")){
                            libraryService.deleteBook(deleteId);
                            System.out.println("=> Książka została usunięta!");}
                            else {System.out.println("Przerwano operację");}
                            break;
                        case "6":
                            authService.logout();
                            System.out.println("=> Wylogowano pomyślnie.");
                            break;
                        case "0":
                            running = false;
                            System.out.println("=> Zamykanie programu. Do widzenia!");
                            break;
                        default:
                            System.out.println("=> Nieznana opcja, spróbuj ponownie.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("=> Błąd: Oczekiwano liczby (ID), a podano tekst!");
                } catch (SecurityException e) {
                    System.out.println("=> Błąd bezpieczeństwa: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("=> Wystąpił nieoczekiwany błąd: " + e.getMessage());
                }
            }
        }
        scanner.close();
    }

    private static void printBooks(List<Book> books) {
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
}