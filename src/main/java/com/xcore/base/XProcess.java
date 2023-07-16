package com.xcore.base;

import com.xcore.message.XMessage;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XProcess extends XTask {
  List<XTask> tasks = new ArrayList<>();

  public XProcess(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  public void addTask(XTask task) {
    this.tasks.add(task);
  }

  @Override
  public Future<JsonObject> execute(JsonObject message) {
    Promise<JsonObject> promise = Promise.promise();
    XMessage newMessage = XMessage.newInstance(message);
    this.next(tasks.iterator(), newMessage)
      .onSuccess(result -> promise.tryComplete(result.getResponse()))
      .onFailure(promise::fail);
    return promise.future();
  }

  private Future<XMessage> next(Iterator<XTask> iterator, XMessage message) {
    if (!iterator.hasNext()) {
      return Future.succeededFuture(message);
    }
    Promise<XMessage> promise = Promise.promise();
    XTask task = iterator.next();
    Future<XMessage> future = task.run(message);
    future
      .onSuccess(result -> promise.handle(this.next(iterator, result)))
      .onFailure(promise::fail);

    return promise.future();
  }
}
