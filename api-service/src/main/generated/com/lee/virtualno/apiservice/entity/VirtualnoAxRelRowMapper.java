package com.lee.virtualno.apiservice.entity;

/**
 * Mapper for {@link VirtualnoAxRel}.
 * NOTE: This class has been automatically generated from the {@link VirtualnoAxRel} original class using Vert.x codegen.
 */
@io.vertx.codegen.annotations.VertxGen
public interface VirtualnoAxRelRowMapper extends io.vertx.sqlclient.templates.RowMapper<VirtualnoAxRel> {

  @io.vertx.codegen.annotations.GenIgnore
  VirtualnoAxRelRowMapper INSTANCE = new VirtualnoAxRelRowMapper() { };

  @io.vertx.codegen.annotations.GenIgnore
  java.util.stream.Collector<io.vertx.sqlclient.Row, ?, java.util.List<VirtualnoAxRel>> COLLECTOR = java.util.stream.Collectors.mapping(INSTANCE::map, java.util.stream.Collectors.toList());

  @io.vertx.codegen.annotations.GenIgnore
  default VirtualnoAxRel map(io.vertx.sqlclient.Row row) {
    VirtualnoAxRel obj = new VirtualnoAxRel();
    Object val;
    int idx;
    if ((idx = row.getColumnIndex("app_id")) != -1 && (val = row.getString(idx)) != null) {
      obj.setAppId((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("area_code")) != -1 && (val = row.getString(idx)) != null) {
      obj.setAreaCode((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("business_id")) != -1 && (val = row.getString(idx)) != null) {
      obj.setBusinessId((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("business_type")) != -1 && (val = row.getString(idx)) != null) {
      obj.setBusinessType((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("created_by")) != -1 && (val = row.getString(idx)) != null) {
      obj.setCreatedBy((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("created_date")) != -1 && (val = row.getLocalDateTime(idx)) != null) {
      obj.setCreatedDate((java.time.LocalDateTime)val);
    }
    if ((idx = row.getColumnIndex("id_virtualno_ax")) != -1 && (val = row.getString(idx)) != null) {
      obj.setIdVirtualnoAx((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("real_number")) != -1 && (val = row.getString(idx)) != null) {
      obj.setRealNumber((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("serial_number")) != -1 && (val = row.getString(idx)) != null) {
      obj.setSerialNumber((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("unbind_plan_date")) != -1 && (val = row.getLocalDateTime(idx)) != null) {
      obj.setUnbindPlanDate((java.time.LocalDateTime)val);
    }
    if ((idx = row.getColumnIndex("unbind_real_date")) != -1 && (val = row.getLocalDateTime(idx)) != null) {
      obj.setUnbindRealDate((java.time.LocalDateTime)val);
    }
    if ((idx = row.getColumnIndex("updated_by")) != -1 && (val = row.getString(idx)) != null) {
      obj.setUpdatedBy((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("updated_date")) != -1 && (val = row.getLocalDateTime(idx)) != null) {
      obj.setUpdatedDate((java.time.LocalDateTime)val);
    }
    if ((idx = row.getColumnIndex("virtual_number")) != -1 && (val = row.getString(idx)) != null) {
      obj.setVirtualNumber((java.lang.String)val);
    }
    return obj;
  }
}
