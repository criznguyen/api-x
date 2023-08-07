package com.xcore.tasks.database.psql;

import com.xcore.base.XUnit;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public abstract class PgExecutorImpl extends XUnit implements PgExecutor {

  public PgExecutorImpl(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }
}
