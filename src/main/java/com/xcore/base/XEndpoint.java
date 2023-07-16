package com.xcore.base;

import com.xcore.annotation.RoutePath;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public abstract class XEndpoint extends XUnit {
  public XEndpoint(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  protected abstract void routingHandle(RoutingContext context);

  private void preHandle(RoutingContext context) {
    this.routingHandle(context);
  }

  public void defineRoute(Router router) throws Exception {
    Class<? extends XEndpoint> clazz = this.getClass();
    if (clazz.isAnnotationPresent(RoutePath.class)) {
        String path = clazz.getAnnotation(RoutePath.class).value();
        String method = clazz.getAnnotation(RoutePath.class).method();
        Route route = router.route(HttpMethod.valueOf(method),path);
        route.handler(this::preHandle);
    } else {
      throw new Exception("RoutePath was not define");
    }
  }

  public void request() {

  }
}
