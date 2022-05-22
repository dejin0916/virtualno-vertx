package com.lee.virtualno.common.database;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class PageResult<T> {
  // 数据总行数
  private long totals;
  // 数据总页数
  private int pages;
  // 当前是第几页
  private int pageNum;
  // 每页显示多少行数据
  private int pageSize;
  // 数据列表
  private List<T> data;

  public PageResult(long totals, int pageNum, int pageSize) {
    super();
    this.totals = totals;
    this.pageNum = pageNum;
    this.pageSize = pageSize;
  }
  public PageResult() {
    super();
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    result.put("totals", getTotals());
    result.put("pages", getPages());
    result.put("pageNum", getPage());
    result.put("pageSize", getSize());
    if (getData() == null) {
      result.put("data", new JsonArray());
    } else {
      result.put("data", getData());
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public PageResult<T> fromJson(JsonObject object) {
    this.totals = object.getInteger("totals");
    this.pages = object.getInteger("pages");
    this.pageNum = object.getInteger("pageNum");
    this.pageSize = object.getInteger("pageSize");
    this.data = object.getJsonArray("data").getList();
    return this;
  }


  public long getTotals() {
    return totals;
  }

  public PageResult<T> setTotals(long totals) {
    this.totals = totals;
    return this;
  }

  public int getPages() {
    if (totals == 0) {
      return 0;
    }
    if (totals % pageSize == 0) {
      pages = (int) (totals / pageSize);
    } else {
      pages = (int) (totals / pageSize) + 1;
    }
    return pages;
  }

  public int getPage() {
    return pageNum;
  }

  public int getSize() {
    return pageSize;
  }

  public List<T> getData() {
    return data;
  }

  public PageResult<T> setData(List<T> data) {
    this.data = data;
    return this;
  }
}
