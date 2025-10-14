package controller;

import domain.Book;
import exceptions.BusinessException;
import service.BookService;
import view.BookView;
import java.util.List;

public class BookController {
    private BookService bookService;
    private BookView bookView;

    public BookController() {
        this.bookService = new BookService();
        this.bookView = new BookView();
    }

    public void addBook() {
        try {
            Book book = bookView.showAddBookForm();
            bookService.addBook(book);
            bookView.showSuccessMessage("Libro agregado exitosamente");
        } catch (BusinessException e) {
            bookView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            bookView.showErrorMessage("Error inesperado: " + e.getMessage());
        }
    }

    public void showAllBooks() {
        List<Book> books = bookService.getAllBooks();
        bookView.displayBooks(books);
    }

    public void searchBooksByTitle() {
        String title = bookView.askForTitle();
        List<Book> books = bookService.searchBooksByTitle(title);
        bookView.displayBooks(books);
    }

    public void searchBooksByAuthor() {
        String author = bookView.askForAuthor();
        List<Book> books = bookService.searchBooksByAuthor(author);
        bookView.displayBooks(books);
    }

    public void updateBook() {
        try {
            String isbn = bookView.askForIsbn();
            Book book = bookService.getBookByIsbn(isbn)
                    .orElseThrow(() -> new BusinessException("Libro no encontrado"));

            Book updatedBook = bookView.showUpdateBookForm(book);
            bookService.updateBook(updatedBook);
            bookView.showSuccessMessage("Libro actualizado exitosamente");

        } catch (BusinessException e) {
            bookView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            bookView.showErrorMessage("Error inesperado: " + e.getMessage());
        }
    }

    public void deleteBook() {
        try {
            String isbn = bookView.askForIsbn();
            Book book = bookService.getBookByIsbn(isbn)
                    .orElseThrow(() -> new BusinessException("Libro no encontrado"));

            if (bookView.confirmDelete(book)) {
                bookService.deleteBook(book.getId());
                bookView.showSuccessMessage("Libro eliminado exitosamente");
            }

        } catch (BusinessException e) {
            bookView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            bookView.showErrorMessage("Error inesperado: " + e.getMessage());
        }
    }
}