package com.xcore.message;

import com.xcore.constants.Resource;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

public class HttpCodec implements MessageCodec<Response, Request> {
  @Override
  public void encodeToWire(Buffer buffer, Response response) {
    JsonObject json = new JsonObject()
      .put("code", response.getCode())
      .put("data", response.getData())
      .put("message", response.getMessage());
    String str = json.encode();
    buffer.appendInt(str.length());
    buffer.appendString(str);
  }

  @Override
  public Request decodeFromWire(int pos, Buffer buffer) {
    int length = buffer.getInt(pos);
    String str = buffer.getString(pos + 4, pos + 4 + length);
    JsonObject json = new JsonObject(str);
    return Request.builder()
      .resource(Resource.valueOf(json.getString("resource")))
      .data(json.getJsonObject("data"))
      .build();
  }

  @Override
  public Request transform(Response response) {
    return Request.builder()
      .data(response.getData())
      .build();
  }

  @Override
  public String name() {
    return "http_codec";
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }
}
