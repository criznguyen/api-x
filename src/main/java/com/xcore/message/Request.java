package com.xcore.message;

import com.xcore.constants.Resource;
import io.vertx.core.json.JsonObject;
import lombok.Builder;

@Builder
public class Request {
  private Resource resource;
  private JsonObject data;
}
