package org.pjgg.services;

import org.pjgg.domain.Person;

import java.util.List;

public interface PersonService {

    String addPerson(Person person);

    List<Person> retrieveAllPeople();
}
