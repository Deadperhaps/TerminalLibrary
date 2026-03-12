package com.deadperhaps.library.ui;

import com.deadperhaps.library.domain.Role;
import com.deadperhaps.library.service.AuthService;
import com.deadperhaps.library.service.LibraryService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TerminalController {
    private final AuthService authService;
    private final LibraryService libraryService;
    private final TerminalView view;

    public void run() {
        boolean running = true;
        view.displayWelcomeMessage();

        while (running) {
            try {
                if (!authService.isLoggedIn()) {
                    running = handleLoginMenu();
                } else {
                    running = handleLibraryMenu();
                }
            } catch (NumberFormatException e) {
                view.displayMessage("=> Błąd: Oczekiwano liczby (ID), a podano tekst!");
            } catch (SecurityException e) {
                view.displayMessage("=> Błąd bezpieczeństwa: " + e.getMessage());
            } catch (Exception e) {
                view.displayMessage("=> Wystąpił nieoczekiwany błąd: " + e.getMessage());
            }
        }
    }

    private boolean handleLoginMenu() {
        view.displayLoginMenu();
        String choice = view.getInput("");

        if ("1".equals(choice)) {
            String username = view.getInput("Podaj login: ");
            String password = view.getInput("Podaj hasło: ");

            if (authService.login(username, password)) {
                view.displayMessage("=> Zalogowano pomyślnie jako: " + username);
            } else {
                view.displayMessage("=> Błędny login lub hasło!");
            }
        } else if ("0".equals(choice)) {
            view.displayMessage("=> Zamykanie programu. Do widzenia!");
            return false;
        } else {
            view.displayMessage("=> Nieznana opcja.");
        }
        return true;
    }

    private boolean handleLibraryMenu() {
        Role currentRole = authService.getLoggedInUser().getRole();
        view.displayLibraryMenu(currentRole);
        String choice = view.getInput("");

        switch (choice) {
            case "1":
                view.displayBooks(libraryService.getAllBooks());
                break;
            case "2":
                String keyword = view.getInput("Podaj fragment tytułu lub nazwisko autora: ");
                view.displayBooks(libraryService.searchBooks(keyword));
                break;
            case "3":
                if (currentRole != Role.ADMIN) { view.displayMessage("=> Brak uprawnień!"); break; }
                handleAddBook();
                break;
            case "4":
                if (currentRole != Role.ADMIN) { view.displayMessage("=> Brak uprawnień!"); break; }
                handleEditBook();
                break;
            case "5":
                if (currentRole != Role.ADMIN) { view.displayMessage("=> Brak uprawnień!"); break; }
                handleDeleteBook();
                break;
            case "6":
                authService.logout();
                view.displayMessage("=> Wylogowano pomyślnie.");
                break;
            case "0":
                view.displayMessage("=> Zamykanie programu. Do widzenia!");
                return false;
            default:
                view.displayMessage("=> Nieznana opcja, spróbuj ponownie.");
        }
        return true;
    }

    private void handleAddBook() {
        String title = view.getInput("Podaj tytuł: ");
        String author = view.getInput("Podaj autora: ");
        if (title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty()) {
            view.displayMessage("=> Błąd: Tytuł i autor nie mogą być puste!");
            return;
        }
        libraryService.addBook(title, author);
        view.displayMessage("=> Książka została dodana!");
    }

    private void handleEditBook() {
        Long editId = view.getLongInput("Podaj ID książki do edycji: ");
        boolean existsToEdit = libraryService.getAllBooks().stream().anyMatch(b -> b.getId().equals(editId));
        if (!existsToEdit) {
            view.displayMessage("=> Błąd: Książka o podanym ID nie istnieje w bibliotece!");
            return;
        }
        String newTitle = view.getInput("Podaj nowy tytuł: ");
        String newAuthor = view.getInput("Podaj nowego autora: ");
        if (newTitle == null || newTitle.trim().isEmpty() || newAuthor == null || newAuthor.trim().isEmpty()) {
            view.displayMessage("=> Błąd: Tytuł i autor nie mogą być puste!");
            return;
        }
        libraryService.updateBook(editId, newTitle, newAuthor);
        view.displayMessage("=> Książka została zaktualizowana!");
    }

    private void handleDeleteBook() {
        Long deleteId = view.getLongInput("Podaj ID książki do usunięcia: ");
        boolean existsToDelete = libraryService.getAllBooks().stream().anyMatch(b -> b.getId().equals(deleteId));
        if (!existsToDelete) {
            view.displayMessage("=> Błąd: Książka o podanym ID nie istnieje w bibliotece!");
            return;
        }
        view.displayMessage("Czy na pewno chcesz usunąć książkę o ID: " + deleteId + " ?");
        view.displayMessage("1. Tak");
        view.displayMessage("2. Nie");
        String confirm = view.getInput("Twój Wybór: ");
        if ("1".equals(confirm)) {
            libraryService.deleteBook(deleteId);
            view.displayMessage("=> Książka została usunięta!");
        } else {
            view.displayMessage("Przerwano operację");
        }
    }
}