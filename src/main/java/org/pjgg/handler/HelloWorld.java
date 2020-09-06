package org.pjgg.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.reactivex.ext.web.RoutingContext;

public class HelloWorld {

    public void helloWorld(final RoutingContext context) {

        String name = context.request().getParam("name");
        context.response()
                .putHeader("content-type", "text/plain")
                .setStatusCode(HttpResponseStatus.OK.code())
                .end(String.format("hello world, %s!", name));
    }
}
