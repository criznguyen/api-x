package com.xcore.tasks.database.psql;

import com.xcore.base.XUnit;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Tuple;

import static com.xcore.constants.ConfigKeys.*;

public class PgQueue extends XUnit {
  private final PgPool client;
  private final String queueName;

  public PgQueue(Vertx vertx, JsonObject config, String queueName) {
    super(vertx, config);
    this.queueName = queueName;
    JsonObject dbConfig = config.getJsonObject(DATABASE, new JsonObject());

    PgConnectOptions connectOptions = new PgConnectOptions()
      .setHost(dbConfig.getString(DB_HOST, "localhost"))
      .setPort(dbConfig.getInteger(DB_PORT, 5432))
      .setDatabase(dbConfig.getString(DB_NAME, "xdb"))
      .setUser(dbConfig.getString(DB_USER, "xdb_user"))
      .setPassword(dbConfig.getString(DB_PASSWORD, "12345678"));

    // Configure the connection pool options
    PoolOptions poolOptions = new PoolOptions().setMaxSize(dbConfig.getInteger(DB_CONNECTION_POOL, 100));
    client = PgPool.pool(vertx, connectOptions, poolOptions);
    initQueueTable(queueName);
  }

  private Future<Void> initQueueTable(String queueName) {
    Promise<Void> promise = Promise.promise();
    String checkExist = "SELECT 1 FROM pg_tables WHERE tablename = $1";
    client.preparedQuery(checkExist).execute(Tuple.of(queueName)).onSuccess(Present -> {
      String createTable = "CREATE TABLE $1 (id SERIAL PRIMARY KEY, message TEXT NOT NULL);";
      client.preparedQuery(createTable)
        .execute(Tuple.of(queueName))
        .onSuccess(ar -> promise.complete())
        .onFailure(promise::fail);
    }).onFailure(promise::fail);
    return promise.future();
  }

  public Future<Void> enqueueMessage(String message) {
    Promise<Void> promise = Promise.promise();
    String sql = "INSERT INTO $1 (message) VALUES ($2)";

    client.preparedQuery(sql).execute(Tuple.of(queueName, message)).onSuccess(ar -> promise.complete()).onFailure(promise::fail);
    return promise.future();
  }

  // Dequeue a message from the queue
  public Future<String> dequeueMessage() {
    Promise<String> promise = Promise.promise();
    String sql = "DELETE FROM $1 WHERE id = (SELECT id FROM message_queue ORDER BY id ASC LIMIT 1) RETURNING message";

    client.preparedQuery(sql).execute(Tuple.of(queueName)).onSuccess(ar -> {
      if (ar.size()> 0) {
        String message = ar.iterator().next().getString("message");
        promise.complete(message);
      } else {
        promise.fail("Have no message from queue");
      }
    }).onFailure(promise::fail);
    return promise.future();
  }
}
