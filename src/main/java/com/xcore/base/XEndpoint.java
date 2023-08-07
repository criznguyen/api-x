package com.xcore.base;

import com.xcore.annotation.XController;
import com.xcore.annotation.XPath;
import com.xcore.constants.XHttpMethod;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class XEndpoint extends XUnit {
  public XEndpoint(Vertx vertx, JsonObject config) { super(vertx, config); }

  public void defineRoute(Router router) throws Exception {
    Class<? extends XEndpoint> clazz = this.getClass();
    if (clazz.isAnnotationPresent(XController.class)) {
      String path = clazz.getAnnotation(XController.class).value();
      this.defineSubRoute(router, clazz, path);
    } else {
      throw new Exception("RoutePath was not define.");
    }
  }

  private void defineSubRoute(Router router, Class<? extends XEndpoint> clazz, String path) {
    for (Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(XPath.class)) {
        String subPath = String.format("%s%s", path, method.getAnnotation(XPath.class).value()).replace("//", "/");
        XHttpMethod xMethod = method.getAnnotation(XPath.class).method();
        Route route = router.route(HttpMethod.valueOf(xMethod.name()), subPath);
        route.handler(routingContext -> this.handleMethodCall(method, routingContext));
      }
    }
  }

  private void handleMethodCall(Method method, RoutingContext routingContext) {
    try {
      System.out.println(routingContext.body().asString());
      method.invoke(this, routingContext);
    } catch (IllegalAccessException | InvocationTargetException e) {
      routingContext.fail(500, e);
    }
  }
}
