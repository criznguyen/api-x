package com.xcore.message.database;

import com.xcore.constants.DbRequestType;
import io.vertx.core.json.JsonObject;
import lombok.Data;

@Data
public class DatabaseMessage {
  private DbRequestType type;
  private String statement;
  private JsonObject data;
}
