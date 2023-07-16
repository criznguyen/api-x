package com.xcore.message;

import com.xcore.constants.MessageType;
import com.xcore.constants.StatusCode;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

public class XMessageCodec implements MessageCodec<XMessage, XMessage> {
  @Override
  public void encodeToWire(Buffer buffer, XMessage message) {
    JsonObject json = new JsonObject()
      .put("code", message.getCode().name())
      .put("message", message.getMessage())
      .put("data", message.getData())
      .put("response", message.getResponse())
      .put("type", message.getType().name());

    String str = json.encode();
    buffer.appendInt(str.length());
    buffer.appendString(str);
  }

  @Override
  public XMessage decodeFromWire(int pos, Buffer buffer) {
    int length = buffer.getInt(pos);
    String str = buffer.getString(pos + 4, pos + 4 + length);
    JsonObject json = new JsonObject(str);
    return XMessage.builder()
      .type(MessageType.valueOf(json.getString("type")))
      .code(StatusCode.valueOf(json.getString("code")))
      .message(json.getString("message"))
      .response(json.getJsonObject("response"))
      .data(json.getJsonObject("data"))
      .build();
  }

  @Override
  public XMessage transform(XMessage message) {
    return message;
  }

  @Override
  public String name() {
    return "x_message_codec";
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }
}
