package com.xcore;

import com.xcore.message.HttpCodec;
import com.xcore.message.XMessage;
import com.xcore.message.XMessageCodec;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;

import static com.xcore.utils.PortKiller.killProcessOnPort;

public class XLauncher extends Launcher {
  public static void main(String[] args) {
    new XLauncher().dispatch(args);
  }

  @Override
  public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
    int port = deploymentOptions.getConfig().getInteger("http-port", 8080);
    killProcessOnPort(port);
    System.out.printf("Kill port %s successfully%n", port);
  }

  @Override
  public void afterStartingVertx(Vertx vertx) {
    vertx.eventBus().registerCodec(new HttpCodec());
    vertx.eventBus().registerDefaultCodec(XMessage.class, new XMessageCodec());
  }
}
