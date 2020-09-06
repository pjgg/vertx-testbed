package org.pjgg.adapters;

import org.pjgg.domain.Person;
import org.pjgg.dto.AddPersonReqDto;
import org.pjgg.dto.PersonDto;

import java.util.List;
import java.util.stream.Collectors;

public interface PersonAdapters {

    default Person toPerson(AddPersonReqDto personDto) {
        return new Person(personDto.getName(), personDto.getLastName(), personDto.getAge());
    }

    default PersonDto toPersonDto(Person person) {
        var dto = new PersonDto();
        dto.setAge(person.getAge());
        dto.setID(person.getID());
        dto.setLastName(person.getLastName());
        dto.setName(person.getName());

        return dto;
    }

    default List<PersonDto> toPeopleDto(List<Person> people) {
        return people.stream().map(person -> toPersonDto(person)).collect(Collectors.toList());
    }
}
