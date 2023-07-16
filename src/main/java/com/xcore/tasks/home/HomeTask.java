package com.xcore.tasks.home;

import com.xcore.base.XTask;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class HomeTask extends XTask {
  public HomeTask(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  @Override
  public Future<JsonObject> execute(JsonObject message) {
    return null;
  }
}
