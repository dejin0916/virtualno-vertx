package com.lee.virtualno.apiservice.entity;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link com.lee.virtualno.apiservice.entity.VirtualnoAxRel}.
 * NOTE: This class has been automatically generated from the {@link com.lee.virtualno.apiservice.entity.VirtualnoAxRel} original class using Vert.x codegen.
 */
public class VirtualnoAxRelConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, VirtualnoAxRel obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "appId":
          if (member.getValue() instanceof String) {
            obj.setAppId((String)member.getValue());
          }
          break;
        case "areaCode":
          if (member.getValue() instanceof String) {
            obj.setAreaCode((String)member.getValue());
          }
          break;
        case "businessId":
          if (member.getValue() instanceof String) {
            obj.setBusinessId((String)member.getValue());
          }
          break;
        case "businessType":
          if (member.getValue() instanceof String) {
            obj.setBusinessType((String)member.getValue());
          }
          break;
        case "createdBy":
          if (member.getValue() instanceof String) {
            obj.setCreatedBy((String)member.getValue());
          }
          break;
        case "idVirtualnoAx":
          if (member.getValue() instanceof String) {
            obj.setIdVirtualnoAx((String)member.getValue());
          }
          break;
        case "realNumber":
          if (member.getValue() instanceof String) {
            obj.setRealNumber((String)member.getValue());
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

  public static void toJson(VirtualnoAxRel obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(VirtualnoAxRel obj, java.util.Map<String, Object> json) {
    if (obj.getAppId() != null) {
      json.put("appId", obj.getAppId());
    }
    if (obj.getAreaCode() != null) {
      json.put("areaCode", obj.getAreaCode());
    }
    if (obj.getBusinessId() != null) {
      json.put("businessId", obj.getBusinessId());
    }
    if (obj.getBusinessType() != null) {
      json.put("businessType", obj.getBusinessType());
    }
    if (obj.getCreatedBy() != null) {
      json.put("createdBy", obj.getCreatedBy());
    }
    if (obj.getIdVirtualnoAx() != null) {
      json.put("idVirtualnoAx", obj.getIdVirtualnoAx());
    }
    if (obj.getRealNumber() != null) {
      json.put("realNumber", obj.getRealNumber());
    }
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
