package com.xcore.tasks.database.psql;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Query;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;

public class PgNotiSender extends PgExecutorImpl {

  private static PgNotiSender instance;
  private final PgPool client;
  private PgNotiSender(Vertx vertx, JsonObject config) {
    super(vertx, config);

    // Configure the connection pool options
    client = PgPool.pool(vertx, getOptions(config), getPoolOptions(config));
  }

  public static PgNotiSender getInstance(Vertx vertx, JsonObject config) {
    if (instance == null) instance = new PgNotiSender(vertx, config);
    return instance;
  }

  public Future<RowSet<Row>> sendNotification(String channel, String message) {
    Promise<RowSet<Row>> promise = Promise.promise();
    Future<SqlConnection> future = client.getConnection();
    future.onSuccess(connection -> {
      Query<RowSet<Row>> res = connection.query(String.format("NOTIFY %s, '%s'", channel, message));
      res.execute(promise);
    }).onFailure(promise::fail);
    return promise.future();
  }
}
