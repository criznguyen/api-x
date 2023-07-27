package com.xcore.tasks.database.psql;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.pubsub.PgSubscriber;

public class PgNotiReceiver extends PgExecutorImpl {
  private static PgNotiReceiver instance;
  private final PgSubscriber subscriber;
  private PgNotiReceiver(Vertx vertx, JsonObject config) {
    super(vertx, config);
    subscriber = PgSubscriber.subscriber(vertx, getOptions());
  }

  public static PgNotiReceiver getInstance(Vertx vertx, JsonObject config) {
    if (instance == null) instance = new PgNotiReceiver(vertx, config);
    return instance;
  }

  public Future<String> subscribeChanel(String chanel) {
    Promise<String> promise = Promise.promise();
    subscriber.channel(chanel).handler(promise::complete);
    return promise.future();
  }
}
