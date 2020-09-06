package org.pjgg.handler;

import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.pjgg.adapters.PersonAdapters;
import org.pjgg.dto.AddPersonReqDto;
import org.pjgg.dto.IdRespDto;
import org.pjgg.services.PersonService;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class People implements PersonAdapters {

    private static final Logger LOGGER = LoggerFactory.getLogger(People.class);
    final PersonService personService;

    public People(final PersonService personService){
        this.personService = personService;
    }

    public void addPeople(final RoutingContext context){
        var person = context.getBodyAsJson().mapTo(AddPersonReqDto.class);
        var ID = personService.addPerson(toPerson(person));

        context.response()
                .putHeader("content-type", "application/json")
                .setStatusCode(OK.code())
                .end(Json.encode(new IdRespDto(ID)));
    }

    public void getPeople(final RoutingContext context){
        var people = personService.retrieveAllPeople();

        context.response()
                .putHeader("content-type", "application/json")
                .setStatusCode(OK.code())
                .end(Json.encode(toPeopleDto(people)));
    }
}
