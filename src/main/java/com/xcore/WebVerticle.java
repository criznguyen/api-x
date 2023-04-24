//package com.xcore;
//
//import io.vertx.core.AbstractVerticle;
//import io.vertx.core.Promise;
//import io.vertx.core.http.HttpServer;
//import io.vertx.ext.web.Router;
//
//public class WebVerticle extends AbstractVerticle {
//  @Override
//  public void start(Promise<Void> startPromise) throws Exception {
//    HttpServer server = vertx.createHttpServer();
//
//    Router router = Router.router(vertx);
//    vertx.createHttpServer().requestHandler(req -> {
//      req.response()
//        .putHeader("content-type", "text/plain")
//        .end("Hello from Vert.x!");
//    }).listen(config().getInteger("http-port", 8888), http -> {
//      if (http.succeeded()) {
//        startPromise.complete();
//        System.out.println("HTTP server started on port 8888");
//      } else {
//        startPromise.fail(http.cause());
//      }
//    });
//  }
//
//
//}
