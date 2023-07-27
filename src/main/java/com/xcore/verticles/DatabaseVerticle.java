package com.xcore.verticles;

import com.xcore.base.XVerticle;
import com.xcore.constants.StatusCode;
import com.xcore.message.XMessage;
import com.xcore.tasks.database.psql.PostgresqlOperations;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import static com.xcore.constants.VerticleNames.DB_VERTICLE;

public class DatabaseVerticle extends XVerticle {
  PostgresqlOperations client;
  @Override
  protected void process(Message<XMessage> message) {
    XMessage data = message.body();
    data.setResponse(new JsonObject().put("result", new JsonObject()));
    data.setCode(StatusCode.SUCCESS);
    message.reply(data);
  }

  @Override
  protected Future<Boolean> beforeConsume() {
    client = new PostgresqlOperations(vertx, config());
    return super.beforeConsume();
  }

  @Override
  protected String getAddress() {
    return DB_VERTICLE;
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    if (client != null) {
      client.close();
    }
    super.stop(stopPromise);
  }
}
