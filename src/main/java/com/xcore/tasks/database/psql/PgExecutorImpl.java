package com.xcore.tasks.database.psql;

import com.xcore.base.XUnit;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import lombok.Getter;

import static com.xcore.constants.ConfigKeys.*;

public abstract class PgExecutorImpl extends XUnit implements PgExecutor {
  @Getter
  private final PgConnectOptions options;
  @Getter
  private final PoolOptions poolOptions;

  public PgExecutorImpl(Vertx vertx, JsonObject config) {
    super(vertx, config);
    JsonObject dbConfig = config.getJsonObject(DATABASE, new JsonObject());
    options = new PgConnectOptions()
      .setHost(dbConfig.getString(DB_HOST, "localhost"))
      .setPort(dbConfig.getInteger(DB_PORT, 5432))
      .setDatabase(dbConfig.getString(DB_NAME, "xdb"))
      .setUser(dbConfig.getString(DB_USER, "xdb_user"))
      .setPassword(dbConfig.getString(DB_PASSWORD, "12345678"));
    poolOptions = new PoolOptions().setMaxSize(dbConfig.getInteger(DB_CONNECTION_POOL, 100));
  }
}
