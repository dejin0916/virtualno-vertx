package com.lee.virtualno.apiservice.request;

public class AxbBindRequest extends BindRequest {
  private String realNumberA;
  private String realNumberB;

  public String getRealNumberA() {
    return realNumberA;
  }

  public void setRealNumberA(String realNumberA) {
    this.realNumberA = realNumberA;
  }

  public String getRealNumberB() {
    return realNumberB;
  }

  public void setRealNumberB(String realNumberB) {
    this.realNumberB = realNumberB;
  }
}
