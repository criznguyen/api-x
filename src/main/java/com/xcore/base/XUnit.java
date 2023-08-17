package com.xcore.base;

import io.vertx.core.Vertx;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
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

  private final Logger logger;


  public XUnit(Vertx vertx, JsonObject config) {
    this.vertx = vertx;
    this.config = config;
    sharedData = vertx.sharedData();
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

    // Get the root logger
    logger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
    System.out.println("logger class: " + logger.getClass());
  }

  protected void info(String message) {
    logger.info(message);
  }

  protected void error(String message) {
    logger.info(message);
  }

  protected void error(String message, Throwable throwable) {
    logger.error(message, throwable);
  }
}
