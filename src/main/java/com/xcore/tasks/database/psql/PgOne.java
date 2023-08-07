package com.xcore.tasks.database.psql;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

import static com.xcore.constants.ConfigKeys.*;

/**
 * PgOne is a singleton class to support singleton postgres connection
 */
public class PgOne {
  private static PgPool clientOne;
  public static synchronized PgPool singleClient(Vertx vertx, JsonObject config) {
    if (clientOne == null) {
      clientOne = multipleClient(vertx, config);
    }
    return clientOne;
  }

  public static PgPool multipleClient(Vertx vertx, JsonObject config) {
    return PgPool.pool(getConnectOptions(config), getPoolOptions(config));
  }

  /**
   *
   * @param config {@link JsonObject}
   * @return {@link PgConnectOptions}
   */
  public static PgConnectOptions getConnectOptions(JsonObject config) {
    JsonObject dbConfig = config.getJsonObject(DATABASE, new JsonObject());
    return new PgConnectOptions()
      .setHost(dbConfig.getString(DB_HOST, "localhost"))
      .setPort(dbConfig.getInteger(DB_PORT, 5432))
      .setDatabase(dbConfig.getString(DB_NAME, "postgres"))
      .setUser(dbConfig.getString(DB_USER, "postgres"))
      .setPassword(dbConfig.getString(DB_PASSWORD, "example"));
  }

  /**
   *
   * @return {@link PgConnectOptions}
   */
  public static PgConnectOptions getConnectOptions() {
    return PgConnectOptions.fromEnv();
  }

  /**
   *
   * @param uri connection string
   * @return {@link PgConnectOptions}
   */
  public static PgConnectOptions getConnectOptions(String uri) {
    return PgConnectOptions.fromUri(uri);
  }

  public static PoolOptions getPoolOptions(JsonObject config) {
    JsonObject dbConfig = config.getJsonObject(DATABASE, new JsonObject());
    return new PoolOptions().setMaxSize(dbConfig.getInteger(DB_CONNECTION_POOL, 100));
  }
}
