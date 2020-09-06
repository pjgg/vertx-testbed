package org.pjgg.services;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.pjgg.domain.Person;

import java.util.List;

public class PersonServiceImpl implements PersonService, PersonCollection {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);
    private final List<Person> repository;

    public PersonServiceImpl(final List<Person> repository) {
        this.repository = repository;
    }

    @Override
    public String addPerson(Person person) {
        return save(person);
    }

    @Override
    public List<Person> retrieveAllPeople() {
        return repository;
    }

    @Override
    public List<Person> getInstance() {
        return repository;
    }
}
