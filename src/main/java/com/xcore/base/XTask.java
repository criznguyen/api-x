package com.xcore.base;

import com.xcore.constants.StatusCode;
import com.xcore.message.XMessage;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;

import static com.xcore.constants.ConfigKeys.TIMEOUT;

public abstract class XTask extends XUnit {
  public XTask(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }
  protected StatusCode code;

  public abstract Future<JsonObject> execute(JsonObject message);
  public Future<JsonObject> executeWithLog(JsonObject message) {
    Promise<JsonObject> promise = Promise.promise();
    Future<JsonObject> future = this.execute(message);
    future.onSuccess(promise::complete);
    future.onFailure(throwable -> {
      getLogger().error(throwable);
      promise.fail(throwable);
    });
    return promise.future();
  }

  public Future<XMessage> run(XMessage message) {
    Promise<XMessage> promise = Promise.promise();
    this.getVertx().setTimer(getConfig().getInteger(TIMEOUT, 30000), handler -> promise.tryFail("Timeout!!"));
    this.executeWithLog(message.getData())
      .onSuccess(result -> promise.tryComplete(message.succeed(result)))
      .onFailure(promise::fail);
    return promise.future();
  }
}
