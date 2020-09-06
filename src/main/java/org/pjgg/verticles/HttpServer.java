package org.pjgg.verticles;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.reactivex.ext.web.handler.*;
import org.pjgg.handler.HealthCheck;
import org.pjgg.handler.HelloWorld;
import org.pjgg.handler.People;
import org.pjgg.services.PersonService;
import org.pjgg.services.PersonServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;

public class HttpServer extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    final static String DEFAULT_HOST = "0.0.0.0";
    final static Integer DEFAULT_PORT = 8080;

    private HealthCheck healthCheck;
    private HelloWorld helloWorld;
    private People people;

    @Override
    public Completable rxStart() {
        vertx.exceptionHandler(error -> LOGGER.info(
                error.getMessage() + error.getCause() + Arrays.toString(error.getStackTrace()) + error.getLocalizedMessage()));

        healthCheck = new HealthCheck();
        helloWorld = new HelloWorld();

        PersonService personService =  new PersonServiceImpl(new ArrayList<>());
        people = new People(personService);

        return OpenAPI3RouterFactory
                .rxCreate(vertx, "webroot/swagger/swagger.yml")
                .doOnError(LOGGER::error)
                .map(routerFactory -> {
                    addGlobalHandlers(routerFactory);
                    routeHandlersBySwaggerOperationId(routerFactory);
                    Router router = routerFactory.getRouter();
                    vanillaRouting(router);
                    routeCommonsHandlers(router);
                    return router;
                }).flatMap(router -> startHttpServer(DEFAULT_HOST, DEFAULT_PORT, router)).flatMapCompletable(httpServer -> {
            LOGGER.info("HTTP server started on http://{0}:{1}", DEFAULT_HOST, DEFAULT_PORT);
            return Completable.complete();
        });

    }

    private void vanillaRouting(Router router) {
        router.get("/health").handler(healthCheck::check);
    }

    private void routeHandlersBySwaggerOperationId(OpenAPI3RouterFactory routerFactory) {
        routerFactory.addHandlerByOperationId("helloByName", helloWorld::helloWorld);
        routerFactory.addHandlerByOperationId("addPerson", people::addPeople);
        routerFactory.addHandlerByOperationId("getPeople", people::getPeople);
    }

    private void addGlobalHandlers(OpenAPI3RouterFactory routerFactory) {
        routerFactory
                .addGlobalHandler(CorsHandler.create("*"))
                .addGlobalHandler(LoggerHandler.create())
                .addGlobalHandler(ResponseTimeHandler.create())
                .addGlobalHandler(BodyHandler.create());
    }

    private void routeCommonsHandlers(Router router) {
        //router.route().failureHandler(new DefaultErrorHandler());
        router.get("/*").handler(StaticHandler.create());
    }

    private Single<io.vertx.reactivex.core.http.HttpServer> startHttpServer(String httpHost, Integer httpPort, Router router) {
        return vertx.createHttpServer().requestHandler(router).rxListen(httpPort, httpHost);
    }
}
