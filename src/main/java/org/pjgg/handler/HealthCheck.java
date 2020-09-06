package org.pjgg.handler;

import io.vertx.core.json.Json;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.pjgg.dto.HealthDTO;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class HealthCheck {

    public void check(final RoutingContext context) {
        context.response()
                .putHeader("content-type", "application/json")
                .setStatusCode(OK.code())
                .end(Json.encode(new HealthDTO(OK.code(), "perfect!")));
    }
}
