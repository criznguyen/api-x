package com.xcore.message;

import com.xcore.constants.StatusCode;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response {
  private StatusCode code;
  private String message;
  private JsonObject data;

  public static Response fromMessage(XMessage message) {
    return Response.builder()
      .code(message.getCode())
      .data(message.getResponse())
      .message(message.getMessage())
      .build();
  }
}
