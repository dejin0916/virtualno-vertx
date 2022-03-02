package com.ldj.virtualno.appInfoservice.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.templates.annotations.ParametersMapped;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serializable;

@DataObject(generateConverter = true)
@RowMapped
@ParametersMapped
public class VirtualNoApp implements Serializable {
  private String id;
  private String appId;
  private String appName;
  private String appKey;
  private String secret;

  public VirtualNoApp() {
  }
  // 必须有JsonObject作为参数的构造函数
  public VirtualNoApp(JsonObject obj) {
    VirtualNoAppConverter.fromJson(obj, this);
  }
  // 生成代理类时必须有toJson这个方法，详见 https://github.com/vert-x3/vertx-codegen/pull/304
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    VirtualNoAppConverter.toJson(this, json);
    return json;
  }

  public VirtualNoApp(String id, String appId, String appName, String appKey, String secret) {
    this.id = id;
    this.appId = appId;
    this.appName = appName;
    this.appKey = appKey;
    this.secret = secret;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getAppKey() {
    return appKey;
  }

  public void setAppKey(String appKey) {
    this.appKey = appKey;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }
}
