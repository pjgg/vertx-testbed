package org.pjgg.services;

import org.pjgg.domain.Person;

import java.util.List;
import java.util.UUID;

@FunctionalInterface
public interface PersonCollection {

    default String save(Person person){
        var ID = UUID.randomUUID().toString();
        person.setID(ID);
        getInstance().add(person);

        return ID;
    }

    List<Person> getInstance();
}
