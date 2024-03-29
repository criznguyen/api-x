package com.xcore;

import com.xcore.route.RootRoute;
import com.xcore.verticles.DatabaseVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

import java.io.IOException;

import static com.xcore.constants.ConfigKeys.HTTP_PORT;

public class MainVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    deploy();
    web(startPromise);
  }

  private void web(Promise<Void> startPromise) {
    int port = config().getInteger( HTTP_PORT, 8080);
    HttpServer server = vertx.createHttpServer();
    Future<Router> future = RootRoute.builder().vertx(vertx).config(config()).build().buildRouter();
    future.onSuccess(router -> server.requestHandler(router).listen(port, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started at port: "+ port);
      } else {
        startPromise.fail(http.cause());
      }
    }));
  }

  private void listenConfigChange() {

  }

  private void deploy() {
    vertx.deployVerticle(new DatabaseVerticle());
  }
}
