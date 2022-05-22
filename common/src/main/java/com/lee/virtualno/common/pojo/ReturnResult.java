package com.lee.virtualno.common.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.core.json.Json;

import java.io.Serializable;

/**
 * <a href="https://coolshell.cn/articles/21672.html">https://coolshell.cn/articles/21672.html</a>
 * 参见这篇文章的观点和评论区的争论，针对有些不好用http状态码表示的业务返回信息，body中设置
 * @param <T>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReturnResult<T> implements Serializable {
  private static final long serialVersionUID = 1L;

  private String code;


  private String msg;


  private T data;

  public static String success() {
    return result(null, ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
  }

  public static <T> String success(T data) {
    return result(data, ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
  }

  public static <T> String success(T data, String msg) {
    return result(data, ResponseCode.SUCCESS.getCode(), msg);
  }

  public static String failed() {
    return result(null, ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
  }

  public static String failed(String msg) {
    return result(null, ResponseCode.ERROR.getCode(), msg);
  }

  public static String failed(String code, String msg) {
    return result(null, code, msg);
  }

  public static <T> String failed(T data) {
    return result(data, ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
  }

  public static <T> String failed(T data, String msg) {
    return result(data, ResponseCode.ERROR.getCode(), msg);
  }

  private static <T> String result(T data, String code, String msg) {
    ReturnResult<T> result = new ReturnResult<>();
    result.setCode(code);
    result.setData(data);
    result.setMsg(msg);
    return Json.encodePrettily(result);
  }


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
