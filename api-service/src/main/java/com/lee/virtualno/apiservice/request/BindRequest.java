package com.lee.virtualno.apiservice.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BindRequest implements Serializable {
  private String appId;

  /**
   * 业务类型
   */
  private String businessType;
  /**
   * 业务ID
   */
  private String businessId;
  /**
   * 号码池编号
   */
  private String serialNumber;
  /**
   * 区域匹配模式 0-非严格匹配，1-严格匹配
   */
  private String areaMatchMode;
  /**
   * 区号
   */
  private String areaCode;
  /**
   * 预期解绑时间
   */
  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime unbindPlanDate;

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getBusinessType() {
    return businessType;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }

  public String getBusinessId() {
    return businessId;
  }

  public void setBusinessId(String businessId) {
    this.businessId = businessId;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getAreaMatchMode() {
    return areaMatchMode;
  }

  public void setAreaMatchMode(String areaMatchMode) {
    this.areaMatchMode = areaMatchMode;
  }

  public String getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

  public LocalDateTime getUnbindPlanDate() {
    return unbindPlanDate;
  }

  public void setUnbindPlanDate(LocalDateTime unbindPlanDate) {
    this.unbindPlanDate = unbindPlanDate;
  }
}
