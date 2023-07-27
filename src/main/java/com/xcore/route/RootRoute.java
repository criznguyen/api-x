package com.xcore.route;

import com.xcore.annotation.AnnotationScanner;
import com.xcore.annotation.XPath;
import com.xcore.base.XEndpoint;
import com.xcore.constants.StatusCode;
import com.xcore.message.Response;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class RootRoute {
  private Vertx vertx;
  private JsonObject config;
  public Future<Router> buildRouter() {
    Promise<Router> promise = Promise.promise();
    Router router = Router.router(vertx);
    this.routes().forEach(route -> {
      try {
        route.defineRoute(router);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    router.route().handler(routingContext -> {
      routingContext.response().setStatusCode(404);
      routingContext.json(Response.builder().code(StatusCode.NOT_FOUND).message("endpoint is not found!").build());
    });
    promise.complete(router);
    return promise.future();
  }

  private List<XEndpoint> routes() {
    List<Class<?>> annotatedClasses = AnnotationScanner.scanClassesWithAnnotation("com.xcore.route", XPath.class);
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
