package com.xcore.base;

import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.SharedData;
import lombok.Getter;

public abstract class XUnit {

  @Getter
  private final Vertx vertx;

  @Getter
  private final JsonObject config;

  @Getter
  SharedData sharedData;

  @Getter
  private final Logger logger;


  public XUnit(Vertx vertx, JsonObject config) {
    this.vertx = vertx;
    this.config = config;
    sharedData = vertx.sharedData();
    logger = LoggerFactory.getLogger(this.getClass());
  }
}
