package org.pjgg;

import io.netty.channel.DefaultChannelId;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import org.pjgg.verticles.HttpServer;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        DefaultChannelId.newInstance();
        DeploymentOptions defaultDeploymentOptions = new DeploymentOptions();
        defaultDeploymentOptions.setInstances(1);
        VertxOptions vertxOptions = new VertxOptions();

        final Vertx vertx = Vertx.vertx(vertxOptions);
        vertx.rxDeployVerticle(HttpServer.class.getName(), defaultDeploymentOptions).subscribe(verticleID -> {
            LOGGER.info(String.format("verticle ID %s deployed!", verticleID));
        });
    }

}
