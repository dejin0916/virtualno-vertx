package com.lee.virtualno.appInfoservice.entity;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link com.lee.virtualno.appInfoservice.entity.VirtualNoApp}.
 * NOTE: This class has been automatically generated from the {@link com.lee.virtualno.appInfoservice.entity.VirtualNoApp} original class using Vert.x codegen.
 */
public class VirtualNoAppConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, VirtualNoApp obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "appId":
          if (member.getValue() instanceof String) {
            obj.setAppId((String)member.getValue());
          }
          break;
        case "appKey":
          if (member.getValue() instanceof String) {
            obj.setAppKey((String)member.getValue());
          }
          break;
        case "appName":
          if (member.getValue() instanceof String) {
            obj.setAppName((String)member.getValue());
          }
          break;
        case "createdBy":
          if (member.getValue() instanceof String) {
            obj.setCreatedBy((String)member.getValue());
          }
          break;
        case "idVirtualnoAppInfo":
          if (member.getValue() instanceof String) {
            obj.setIdVirtualnoAppInfo((String)member.getValue());
          }
          break;
        case "secret":
          if (member.getValue() instanceof String) {
            obj.setSecret((String)member.getValue());
          }
          break;
        case "updatedBy":
          if (member.getValue() instanceof String) {
            obj.setUpdatedBy((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(VirtualNoApp obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(VirtualNoApp obj, java.util.Map<String, Object> json) {
    if (obj.getAppId() != null) {
      json.put("appId", obj.getAppId());
    }
    if (obj.getAppKey() != null) {
      json.put("appKey", obj.getAppKey());
    }
    if (obj.getAppName() != null) {
      json.put("appName", obj.getAppName());
    }
    if (obj.getCreatedBy() != null) {
      json.put("createdBy", obj.getCreatedBy());
    }
    if (obj.getIdVirtualnoAppInfo() != null) {
      json.put("idVirtualnoAppInfo", obj.getIdVirtualnoAppInfo());
    }
    if (obj.getSecret() != null) {
      json.put("secret", obj.getSecret());
    }
    if (obj.getUpdatedBy() != null) {
      json.put("updatedBy", obj.getUpdatedBy());
    }
  }
}
