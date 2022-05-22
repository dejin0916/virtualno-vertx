package com.lee.virtualno.common.codec;

import com.lee.virtualno.common.database.PageResult;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * 自定义分页查询实体的codec
 * app模块选择使用eventbus生成的proxy
 * eventbus针对这种实体类需要自定义codec，否则无法解析数据
 * <a href="https://github.com/eclipse-vertx/vert.x">详见vertx的测试用例</a>
 */
public class PageResultCodec implements MessageCodec<JsonObject, PageResult<Object>> {
  @Override
  public void encodeToWire(Buffer buffer, JsonObject object) {
    byte[] bytes = object.toBuffer().getBytes();
    buffer.appendInt(bytes.length);
    buffer.appendBytes(bytes);
  }

  @Override
  public PageResult<Object> decodeFromWire(int pos, Buffer buffer) {
    int length = buffer.getInt(pos);
    pos += 4;
    byte[] bytes = buffer.getBytes(pos, pos + length);
    JsonObject object = new JsonObject(Buffer.buffer(bytes));
    return new PageResult<>().fromJson(object);
  }

  @Override
  public PageResult<Object> transform(JsonObject object) {
    return new PageResult<>().fromJson(object);
  }

  @Override
  public String name() {
    return "PageResultCodec";
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }
}
