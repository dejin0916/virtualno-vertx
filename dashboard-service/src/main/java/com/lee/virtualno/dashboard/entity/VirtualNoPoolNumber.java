package com.lee.virtualno.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.templates.annotations.Column;
import io.vertx.sqlclient.templates.annotations.ParametersMapped;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serializable;
import java.time.LocalDateTime;

@DataObject(generateConverter = true)
@RowMapped
@ParametersMapped
public class VirtualNoPoolNumber implements Serializable {

  @Column(name = "serial_number")
  private String serialNumber;

  @Column(name = "virtual_number")
  private String virtualNumber;

  @Column(name = "area_code")
  private String areaCode;

  @Column(name = "virtual_type")
  private String virtualType;

  @Column(name = "enable")
  private boolean enable;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  @Column(name = "updated_date")
  private LocalDateTime updatedDate;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_by")
  private String updatedBy;

  public VirtualNoPoolNumber() {
  }

  public VirtualNoPoolNumber(JsonObject obj) {
    VirtualNoPoolNumberConverter.fromJson(obj, this);
  }
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    VirtualNoPoolNumberConverter.toJson(this, json);
    return json;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getVirtualNumber() {
    return virtualNumber;
  }

  public void setVirtualNumber(String virtualNumber) {
    this.virtualNumber = virtualNumber;
  }

  public String getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

  public String getVirtualType() {
    return virtualType;
  }

  public void setVirtualType(String virtualType) {
    this.virtualType = virtualType;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
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
