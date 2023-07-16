package com.xcore.route;

import com.xcore.annotation.RoutePath;
import com.xcore.base.XEndpoint;
import com.xcore.message.Response;
import com.xcore.message.XMessage;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import static com.xcore.constants.VerticleNames.DB_VERTICLE;

@RoutePath(value = "/home")
public class HomeEndpoint extends XEndpoint {
  public HomeEndpoint(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  @Override
  protected void routingHandle(RoutingContext context) {
    getVertx().eventBus().request(DB_VERTICLE, XMessage.builder().build(), responseData -> {
      if (responseData.succeeded()) {
        context.json(Response.fromMessage((XMessage) responseData.result().body()));
      }
    });
  }
}