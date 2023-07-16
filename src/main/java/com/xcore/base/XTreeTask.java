package com.xcore.base;

import com.xcore.message.XMessage;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public abstract class XTreeTask extends XTask {

  private XTask left;
  private XTask right;

  public XTreeTask(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  public void addTasks(XTask left, XTask right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public Future<XMessage> run(XMessage message) {
    Promise<XMessage> promise = Promise.promise();
    Future<XMessage> future = super.run(message);
    future.onSuccess(result -> {
      XMessage newMessage = XMessage.newInstance(result.getResponse());
      promise.handle(right.run(newMessage));
    }).onFailure(exception -> promise.handle(left.run(message)));
    return promise.future();
  }
}
