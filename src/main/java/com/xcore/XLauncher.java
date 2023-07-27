package com.xcore;

import com.xcore.config.ConfigManager;
import com.xcore.message.HttpCodec;
import com.xcore.message.XMessage;
import com.xcore.message.XMessageCodec;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;

import static com.xcore.constants.ConfigKeys.HTTP_PORT;
import static com.xcore.utils.PortKiller.killProcessOnPort;

public class XLauncher extends Launcher {

  public static void main(String[] args) {
    new XLauncher().dispatch(args);
  }

  @Override
  public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
    int port = deploymentOptions.getConfig().getInteger(HTTP_PORT, 8080);
    killProcessOnPort(port);
    System.out.printf("Kill port %s successfully%n", port);
  }
  private Future<JsonObject> getConfig(Vertx vertx, String path) {
    Promise<JsonObject> promise = Promise.promise();
    ConfigManager.init(vertx);
    return promise.future();
  }

  @Override
  public void afterStartingVertx(Vertx vertx) {
    vertx.eventBus().registerCodec(new HttpCodec());
    vertx.eventBus().registerDefaultCodec(XMessage.class, new XMessageCodec());
  }
}
