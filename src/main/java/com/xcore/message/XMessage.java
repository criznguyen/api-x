package com.xcore.message;

import com.xcore.constants.MessageType;
import com.xcore.constants.StatusCode;
import io.vertx.core.json.JsonObject;
import lombok.*;

import static com.xcore.constants.Keys.EXCEPTION;

@Builder
@Data
public class XMessage extends XObject {
  private StatusCode code;
  private String message;
  private MessageType type;
  private JsonObject data;
  private JsonObject response;

  public static XMessage newInstance(JsonObject data) {
    return XMessage.builder().data(data).build();
  }

  public static XMessage newInstance(JsonObject data, StatusCode code) {
    return XMessage.builder().data(data).code(code).build();
  }

  public void exception(Throwable exception) {
    if (response == null) {
      response = new JsonObject();
    }
    this.code = StatusCode.EXCEPTION;
    response.put(EXCEPTION, exception.getMessage());
  }
  public XMessage succeed(JsonObject response) {
    code = StatusCode.SUCCESS;
    this.response = response;
    return this;
  }

  public XMessage succeed(JsonObject response, StatusCode code) {
    this.code = StatusCode.SUCCESS;
    this.response = response;
    return this;
  }

  public boolean isSuccess() {
    return code == StatusCode.SUCCESS;
  }

  public boolean isFailed() {
    return code != StatusCode.SUCCESS;
  }
}
