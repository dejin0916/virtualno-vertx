package com.lee.virtualno.user.entity;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link com.lee.virtualno.user.entity.VirtualNoUser}.
 * NOTE: This class has been automatically generated from the {@link com.lee.virtualno.user.entity.VirtualNoUser} original class using Vert.x codegen.
 */
public class VirtualNoUserConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, VirtualNoUser obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "contact":
          if (member.getValue() instanceof String) {
            obj.setContact((String)member.getValue());
          }
          break;
        case "createdBy":
          if (member.getValue() instanceof String) {
            obj.setCreatedBy((String)member.getValue());
          }
          break;
        case "nickname":
          if (member.getValue() instanceof String) {
            obj.setNickname((String)member.getValue());
          }
          break;
        case "password":
          if (member.getValue() instanceof String) {
            obj.setPassword((String)member.getValue());
          }
          break;
        case "updatedBy":
          if (member.getValue() instanceof String) {
            obj.setUpdatedBy((String)member.getValue());
          }
          break;
        case "username":
          if (member.getValue() instanceof String) {
            obj.setUsername((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(VirtualNoUser obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(VirtualNoUser obj, java.util.Map<String, Object> json) {
    if (obj.getContact() != null) {
      json.put("contact", obj.getContact());
    }
    if (obj.getCreatedBy() != null) {
      json.put("createdBy", obj.getCreatedBy());
    }
    if (obj.getNickname() != null) {
      json.put("nickname", obj.getNickname());
    }
    if (obj.getPassword() != null) {
      json.put("password", obj.getPassword());
    }
    if (obj.getUpdatedBy() != null) {
      json.put("updatedBy", obj.getUpdatedBy());
    }
    if (obj.getUsername() != null) {
      json.put("username", obj.getUsername());
    }
  }
}
