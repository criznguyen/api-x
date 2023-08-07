package com.xcore.tasks.database.psql;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

public interface PgExecutor {
  default PgPool getClient(Vertx vertx, JsonObject config) {
    return PgOne.singleClient(vertx, config);
  }

  default PgConnectOptions getOptions(JsonObject config) {
    return PgOne.getConnectOptions(config);
  }

  default PoolOptions getPoolOptions(JsonObject config) {
    return PgOne.getPoolOptions(config);
  }
}
