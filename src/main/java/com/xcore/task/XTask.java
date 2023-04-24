package com.xcore.task;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import lombok.Getter;

public abstract class XTask {

  @Getter
  private Vertx vertx;

  @Getter
  private Json config;


  public abstract void execute();
}
