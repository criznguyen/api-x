package com.xcore.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import static com.xcore.constants.ConfigKeys.EnvKeys.CONFIG_FILE_PATH;
import static com.xcore.constants.ConfigKeys.EnvKeys.ENV_CONFIG_PREFIX;


public class ConfigManager {
  private final Vertx vertx;
  private static JsonObject configCache;

  private static ConfigManager instance;

  private ConfigManager(Vertx vertx) {
    this.vertx = vertx;
    env();
  }

  public static ConfigManager init(Vertx vertx) {
    if (instance == null) {
      instance = new ConfigManager(vertx);
    }
    return instance;
  }

  private void env() {
    ConfigStoreOptions sysPropsStore = new ConfigStoreOptions().setType("sys");

    ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(sysPropsStore);

    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    retriever.getConfig().onSuccess(ar -> {
      loadEnvFile(ar);
      ar.stream()
        .filter(env -> env.getKey().startsWith(ENV_CONFIG_PREFIX))
        .forEach(env -> vertx.getOrCreateContext().config().put(env.getKey(), env.getValue()));
    });
  }

  private void loadEnvFile(JsonObject env) {
    String path = env.getString(CONFIG_FILE_PATH);
    if (path == null) {
      return;
    }
    ConfigStoreOptions filePropsStore = new ConfigStoreOptions().setType("file").setOptional(true).setConfig(new JsonObject().put("path", path));
    ConfigRetrieverOptions opts = new ConfigRetrieverOptions().addStore(filePropsStore);
    ConfigRetriever fileRetriever = ConfigRetriever.create(vertx, opts);
    fileRetriever.getConfig().onSuccess(vertx.getOrCreateContext().config()::mergeIn);
  }

  public Future<JsonObject> fetch(String path) {
    Promise<JsonObject> promise = Promise.promise();
    env();
    this.fetch(path, false).onSuccess(promise::complete).onFailure(promise::fail);
    return promise.future();
  }

  public Future<JsonObject> fetch(String path, boolean force) {
    Promise<JsonObject> promise = Promise.promise();
    if (!force && configCache != null) {
      promise.complete(configCache);
    } else {
      ConfigStoreOptions fileStore = new ConfigStoreOptions()
        .setType("file")
        .setOptional(true)
        .setConfig(new JsonObject().put("path", path));
      ConfigStoreOptions sysPropsStore = new ConfigStoreOptions().setType("sys");

      ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore).addStore(sysPropsStore);

      ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
      retriever.getConfig().onComplete(ar -> {
        if (ar.failed()) {
          promise.complete(new JsonObject());
        } else {
          configCache = ar.result();
          promise.complete(configCache);
        }
      });
    }
    return promise.future();
  }

}
