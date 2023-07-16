package com.xcore.base;

import com.xcore.message.XMessage;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.atomic.AtomicInteger;

public class XParallel extends XProcess {
  private final boolean failAccepted;
  public XParallel(Vertx vertx, JsonObject config, boolean failAccepted) {
    super(vertx, config);
    this.failAccepted = failAccepted;
  }

  @Override
  public Future<JsonObject> execute(JsonObject message) {
    Promise<JsonObject> promise = Promise.promise();
    AtomicInteger count = new AtomicInteger(0);
    tasks.parallelStream().forEach(task -> {
      Future<XMessage> future = task.run(XMessage.newInstance(message));
      if (!failAccepted) {
        future.onFailure(promise::fail);
      } else {
        future.onSuccess(result -> {
          int current = count.incrementAndGet();
          if (current == tasks.size()) {
            promise.tryComplete(result.getResponse());
          }
        }).onFailure(throwable -> {
          int current = count.incrementAndGet();
          if (current == tasks.size()) {
            promise.fail(throwable);
          }
        });
      }

    });
    return promise.future();
  }
}
