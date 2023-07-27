package com.xcore.base;

import com.xcore.annotation.XMethod;
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
  public XEndpoint(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  protected abstract void routingHandle(RoutingContext context);

  private void preHandle(RoutingContext context) {
    this.routingHandle(context);
  }

  public void defineRoute(Router router) throws Exception {
    Class<? extends XEndpoint> clazz = this.getClass();
    if (clazz.isAnnotationPresent(XPath.class)) {
      String path = clazz.getAnnotation(XPath.class).value();
      XHttpMethod method = clazz.getAnnotation(XPath.class).method();
      Route route = router.route(HttpMethod.valueOf(method.name()),path);
      route.handler(this::preHandle);
      this.defineSubRoute(router, clazz, path);
    } else {
      throw new Exception("RoutePath was not define.");
    }
  }

  private void defineSubRoute(Router router, Class<? extends XEndpoint> clazz, String path) {
    for (Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(XPath.class)) {
        String subPath = String.format("%s%s", path, method.getAnnotation(XPath.class).value()).replace("//", "/");
        System.out.printf("method: %s%nsubPath: %s, %s%n", method.getName(), subPath, method.getAnnotation(XPath.class).value());
        XHttpMethod xMethod = clazz.getAnnotation(XPath.class).method();
        Route route = router.route(HttpMethod.valueOf(xMethod.name()), subPath);
        route.handler(routingContext -> this.handleMethodCall(method, routingContext));
      }
      if (method.isAnnotationPresent(XMethod.class)) {
        XHttpMethod xMethod = method.getAnnotation(XMethod.class).value();
        Route route = router.route(HttpMethod.valueOf(xMethod.name()), path);
        route.handler(routingContext -> this.handleMethodCall(method, routingContext));
      }
    }
  }

  private void handleMethodCall(Method method, RoutingContext routingContext) {
    try {
      method.invoke(this, routingContext);
    } catch (IllegalAccessException | InvocationTargetException e) {
      routingContext.fail(500, e);
    }
  }
}
