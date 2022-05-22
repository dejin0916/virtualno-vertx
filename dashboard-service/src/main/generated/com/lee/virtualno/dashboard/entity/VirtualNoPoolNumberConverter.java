package com.lee.virtualno.dashboard.entity;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link com.lee.virtualno.dashboard.entity.VirtualNoPoolNumber}.
 * NOTE: This class has been automatically generated from the {@link com.lee.virtualno.dashboard.entity.VirtualNoPoolNumber} original class using Vert.x codegen.
 */
public class VirtualNoPoolNumberConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, VirtualNoPoolNumber obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "areaCode":
          if (member.getValue() instanceof String) {
            obj.setAreaCode((String)member.getValue());
          }
          break;
        case "createdBy":
          if (member.getValue() instanceof String) {
            obj.setCreatedBy((String)member.getValue());
          }
          break;
        case "enable":
          if (member.getValue() instanceof Boolean) {
            obj.setEnable((Boolean)member.getValue());
          }
          break;
        case "serialNumber":
          if (member.getValue() instanceof String) {
            obj.setSerialNumber((String)member.getValue());
          }
          break;
        case "updatedBy":
          if (member.getValue() instanceof String) {
            obj.setUpdatedBy((String)member.getValue());
          }
          break;
        case "virtualNumber":
          if (member.getValue() instanceof String) {
            obj.setVirtualNumber((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(VirtualNoPoolNumber obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(VirtualNoPoolNumber obj, java.util.Map<String, Object> json) {
    if (obj.getAreaCode() != null) {
      json.put("areaCode", obj.getAreaCode());
    }
    if (obj.getCreatedBy() != null) {
      json.put("createdBy", obj.getCreatedBy());
    }
    json.put("enable", obj.isEnable());
    if (obj.getSerialNumber() != null) {
      json.put("serialNumber", obj.getSerialNumber());
    }
    if (obj.getUpdatedBy() != null) {
      json.put("updatedBy", obj.getUpdatedBy());
    }
    if (obj.getVirtualNumber() != null) {
      json.put("virtualNumber", obj.getVirtualNumber());
    }
  }
}
