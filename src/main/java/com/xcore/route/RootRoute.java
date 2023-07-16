package com.xcore.route;

import com.xcore.annotation.AnnotationScanner;
import com.xcore.annotation.RoutePath;
import com.xcore.base.XEndpoint;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.Builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Builder
public class RootRoute {
  private Vertx vertx;
  private JsonObject config;
  public void buildRouter(Handler<Router> handler) {
    Router router = Router.router(vertx);
    this.routes().forEach(route -> {
      try {
        route.defineRoute(router);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    router.route().handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      response.setStatusCode(404);
      response.end("Not Found");
    });
    handler.handle(router);
  }

  private List<XEndpoint> routes() {
    List<Class<?>> annotatedClasses = AnnotationScanner.scanClassesWithAnnotation("com.xcore.route", RoutePath.class);
    List<XEndpoint> instances = new ArrayList<>();

    for (Class<?> clazz : annotatedClasses) {
      try {
        instances.add((XEndpoint) clazz.getDeclaredConstructor(Vertx.class, JsonObject.class).newInstance(vertx, config));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return instances;
  }
}
