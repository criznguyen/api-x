package com.xcore.route;

import com.xcore.annotation.RoutePath;
import com.xcore.base.XEndpoint;
import com.xcore.message.Response;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@RoutePath(value = "/test")
public class TestEndpoint extends XEndpoint {
  public TestEndpoint(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  @Override
  protected void routingHandle(RoutingContext context) {
    Response response = Response.builder().data(new JsonObject("{\"abc\": \"123\"}")).build();
    context.json(response, handle-> {});
  }
//  @Override
//  protected void process(Message<JsonObject> message) {
//    System.out.println("test route reply....");
//    message.reply(new JsonObject().put("a", "b"));
//  }
//
//  @Override
//  protected String getAddress() {
//    return "test";
//  }
}
