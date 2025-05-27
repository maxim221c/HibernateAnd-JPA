package ru.koval.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.koval.springcourse.models.Book;
import ru.koval.springcourse.models.Person;
import ru.koval.springcourse.repositories.BookRepositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepositories bookRepositories;

    @Autowired
    public BookService(BookRepositories bookRepositories) {
        this.bookRepositories = bookRepositories;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return bookRepositories.findAll(Sort.by("year"));
        } else
            return bookRepositories.findAll();
    }

    public List<Book> findPagination(Integer page, Integer booksPerPage, boolean sort) {
        if (sort) {
            return bookRepositories.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        } else
            return bookRepositories.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Book findOne(int id) {
        Optional<Book> book = bookRepositories.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        bookRepositories.save(book);
    }

    @Transactional
    public void update(int id, Book bookUp) {
        Book book = bookRepositories.findById(id).get();
        bookUp.setId(id);
        bookUp.setOwner(book.getOwner());
        bookRepositories.save(bookUp);
    }

    @Transactional
    public void delete(int id) {
        bookRepositories.deleteById(id);
    }

    public Person getBookOwner(int id) {
        return bookRepositories.findById(id).map(Book::getOwner).orElse(null);
    }

    //Освобождает книгу
    @Transactional
    public void release(int id) {
        bookRepositories.findById(id).ifPresent(book -> {
            book.setOwner(null);
            book.setTakenAt(null);
        });
    }

    //Назначает книгу
    @Transactional
    public void assign(int id, Person person) {
        bookRepositories.findById(id).ifPresent(book -> {
            book.setOwner(person);
            book.setTakenAt(new Date());
        });
    }

    public List<Book> searchBook(String title) {
        return bookRepositories.findByTitleStartingWith(title);
    }

}

