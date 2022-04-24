package com.lee.virtualno.common.pojo;

/**
 *
 */
public enum ResponseCode {
  SUCCESS("000000","成功"),
  ERROR("999999","失败"),
  PARAMS_NULL("100001","参数不能为空"),
  PARAMS_ERROR("100002","参数不合法");

  private final String code;
  private final String message;

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  ResponseCode(String code, String message) {
    this.code = code;
    this.message = message;
  }
}
