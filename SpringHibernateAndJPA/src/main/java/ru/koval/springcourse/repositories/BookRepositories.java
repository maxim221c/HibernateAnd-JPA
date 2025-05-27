package ru.koval.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.koval.springcourse.models.Book;

import java.util.List;

@Repository
public interface BookRepositories extends JpaRepository<Book, Integer> {
    List<Book> findByTitleStartingWith(String title);
}
