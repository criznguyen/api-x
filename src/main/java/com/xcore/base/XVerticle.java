package com.xcore.base;

import com.xcore.message.XMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;

public abstract class XVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    this.beforeConsume(result -> {
      if (!result) {
        startPromise.fail("cannot complete step beforeConsume");
      }
      EventBus bus = vertx.eventBus();
      MessageConsumer<XMessage> consumer = bus.consumer(this.getAddress());
      consumer.handler(this::process);
    });
  }

  protected void beforeConsume(Handler<Boolean> handler) {
    handler.handle(true);
  }

  protected abstract void process(Message<XMessage> message);

  protected abstract String getAddress();

}
