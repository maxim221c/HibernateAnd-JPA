package ru.koval.springcourse.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.koval.springcourse.models.Book;
import ru.koval.springcourse.models.Person;
import ru.koval.springcourse.repositories.PeopleRepositories;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepositories peopleRepositories;

    @Autowired
    public PeopleService(PeopleRepositories peopleRepositories) {
        this.peopleRepositories = peopleRepositories;
    }

    public List<Person> findAll() {
        return peopleRepositories.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> person = peopleRepositories.findById(id);
        return person.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepositories.save(person);
    }

    @Transactional
    public void update(int id, Person personUpdate) {
        personUpdate.setId(id);
        peopleRepositories.save(personUpdate);
    }

    @Transactional
    public void delete(int id) {
        peopleRepositories.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepositories.findByFullName(fullName);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepositories.findById(id);
        if (person.isPresent()) {
            long timeExpired = 864_000_000;
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(book -> {
                long value = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (value > timeExpired) {
                    book.setExpired(true);
                }
            });
            return person.get().getBooks();
        }
        return Collections.emptyList();
    }
}
