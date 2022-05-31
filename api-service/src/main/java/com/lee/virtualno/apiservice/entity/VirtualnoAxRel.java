package com.lee.virtualno.apiservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.templates.annotations.Column;
import io.vertx.sqlclient.templates.annotations.ParametersMapped;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@DataObject(generateConverter = true)
@RowMapped
@ParametersMapped
public class VirtualnoAxRel implements Serializable {
  /**
   * 主键
   */
  @Column(name = "id_virtualno_ax")
  private String idVirtualnoAx;

  /**
   * 创建人
   */
  @Column(name = "created_by")
  private String createdBy;

  /**
   * 创建时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  @Column(name = "created_date")
  private LocalDateTime createdDate;

  /**
   * 更新人
   */
  @Column(name = "updated_by")
  private String updatedBy;

  /**
   * 更新时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  @Column(name = "updated_date")
  private LocalDateTime updatedDate;

  /**
   * 计划解绑时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  @Column(name = "unbind_plan_date")
  private LocalDateTime unbindPlanDate;

  /**
   * 实际解绑时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  @Column(name = "unbind_real_date")
  private LocalDateTime unbindRealDate;

  /**
   * 业务ID
   */
  @Column(name = "business_id")
  private String businessId;

  /**
   * 业务类型
   */
  @Column(name = "business_type")
  private String businessType;

  /**
   * 真实号码
   */
  @Column(name = "real_number")
  private String realNumber;

  /**
   * 虚拟号码（隐私号）
   */
  @Column(name = "virtual_number")
  private String virtualNumber;

  /**
   * 接入方ID
   */
  @Column(name = "app_id")
  private String appId;

  /**
   * 号码池编号
   */
  @Column(name = "serial_number")
  private String serialNumber;

  /**
   * 区域编码
   */
  @Column(name = "area_code")
  private String areaCode;

  public VirtualnoAxRel() {
  }

  public VirtualnoAxRel(JsonObject obj) {
    VirtualnoAxRelConverter.fromJson(obj, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    VirtualnoAxRelConverter.toJson(this, json);
    return json;
  }

  public String getIdVirtualnoAx() {
    return idVirtualnoAx;
  }

  public void setIdVirtualnoAx(String idVirtualnoAx) {
    this.idVirtualnoAx = idVirtualnoAx;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public LocalDateTime getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(LocalDateTime updatedDate) {
    this.updatedDate = updatedDate;
  }

  public LocalDateTime getUnbindPlanDate() {
    return unbindPlanDate;
  }

  public void setUnbindPlanDate(LocalDateTime unbindPlanDate) {
    this.unbindPlanDate = unbindPlanDate;
  }

  public LocalDateTime getUnbindRealDate() {
    return unbindRealDate;
  }

  public void setUnbindRealDate(LocalDateTime unbindRealDate) {
    this.unbindRealDate = unbindRealDate;
  }

  public String getBusinessId() {
    return businessId;
  }

  public void setBusinessId(String businessId) {
    this.businessId = businessId;
  }

  public String getBusinessType() {
    return businessType;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }

  public String getRealNumber() {
    return realNumber;
  }

  public void setRealNumber(String realNumber) {
    this.realNumber = realNumber;
  }

  public String getVirtualNumber() {
    return virtualNumber;
  }

  public void setVirtualNumber(String virtualNumber) {
    this.virtualNumber = virtualNumber;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }
}
