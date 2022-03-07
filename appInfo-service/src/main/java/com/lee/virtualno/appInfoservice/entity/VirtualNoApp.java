package com.lee.virtualno.appInfoservice.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.templates.annotations.Column;
import io.vertx.sqlclient.templates.annotations.ParametersMapped;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * codegen还不支持LocalDateTime
 * 详情看这个链接，https://github.com/vert-x3/vertx-codegen/pull/344
 * 不知道什么时候合并到主线
 */
@DataObject(generateConverter = true)
@RowMapped
@ParametersMapped
public class VirtualNoApp implements Serializable {
  @Column(name = "id_virtualno_app_info")
  private String idVirtualnoAppInfo;

  @Column(name = "app_id")
  private String appId;

  @Column(name = "app_name")
  private String appName;

  @Column(name = "app_key")
  private String appKey;

  @Column(name = "secret")
  private String secret;

  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  private LocalDateTime updatedDate;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_by")
  private String updatedBy;

  public VirtualNoApp() {
  }
  // codegen必须有JsonObject作为参数的构造函数
  public VirtualNoApp(JsonObject obj) {
    VirtualNoAppConverter.fromJson(obj, this);
  }
  // codegen必须有toJson这个方法，详见 https://github.com/vert-x3/vertx-codegen/pull/304
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    VirtualNoAppConverter.toJson(this, json);
    return json;
  }

  public VirtualNoApp(String idVirtualnoAppInfo, String appId, String appName, String appKey, String secret, LocalDateTime createdDate, LocalDateTime updatedDate, String createdBy, String updatedBy) {
    this.idVirtualnoAppInfo = idVirtualnoAppInfo;
    this.appId = appId;
    this.appName = appName;
    this.appKey = appKey;
    this.secret = secret;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
    this.createdBy = createdBy;
    this.updatedBy = updatedBy;
  }

  public String getIdVirtualnoAppInfo() {
    return idVirtualnoAppInfo;
  }

  public void setIdVirtualnoAppInfo(String idVirtualnoAppInfo) {
    this.idVirtualnoAppInfo = idVirtualnoAppInfo;
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

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDateTime getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(LocalDateTime updatedDate) {
    this.updatedDate = updatedDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
