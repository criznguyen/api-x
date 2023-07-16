package com.xcore.tasks.database;

import com.xcore.base.XTask;
import com.xcore.constants.DbRequestType;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import static com.xcore.constants.DbStatementKeys.*;
import static com.xcore.constants.EnvKeys.*;

public class PostgresqlOperations extends XTask {

  private final PgPool client;

  public PostgresqlOperations(Vertx vertx, JsonObject config) {
    super(vertx, config);
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
  }

  @Override
  public Future<JsonObject> execute(JsonObject message) {
    Promise<JsonObject> promise = Promise.promise();
    String sql = message.getString(QUERY);
    Tuple params = Tuple.of(message.getJsonArray(PARAMS));
    Future<RowSet<Row>> future = switch (DbRequestType.valueOf(message.getString(DB_REQUEST_TYPE))) {
      case SELECT -> select(sql);
      case INSERT -> insert(sql, params);
      case UPDATE -> update(sql, params);
      case DELETE -> delete(sql, params);
    };

    future.map(this::mapping).onComplete(promise);
    return promise.future();
  }

  public void close() {
    client.close();
  }

  public Future<RowSet<Row>> select(String query) {
    Promise<RowSet<Row>> promise = Promise.promise();
    client.query(query)
      .execute().onComplete(promise);
    return promise.future();
  }

  public Future<RowSet<Row>> insert(String query, Tuple params) {
    Promise<RowSet<Row>> promise = Promise.promise();
    client.preparedQuery(query)
      .execute(params)
      .onComplete(promise);
    return promise.future();
  }

  public Future<RowSet<Row>> update(String query, Tuple params) {
    Promise<RowSet<Row>> promise = Promise.promise();
    client.preparedQuery(query)
      .execute(params)
      .onComplete(promise);
    return promise.future();
  }

  public Future<RowSet<Row>> delete(String query, Tuple params) {
    Promise<RowSet<Row>> promise = Promise.promise();
    client.preparedQuery(query)
      .execute(params)
      .onComplete(promise);
    return promise.future();
  }

  private JsonObject mapping(RowSet<Row> rows) {
    JsonArray array = new JsonArray();
    rows.forEach(row -> array.add(row.toJson()));
    return new JsonObject().put("DATA", array);
  }
}
