package com.ldj.virtualno.appInfoservice.entity;

/**
 * Mapper for {@link VirtualNoApp}.
 * NOTE: This class has been automatically generated from the {@link VirtualNoApp} original class using Vert.x codegen.
 */
@io.vertx.codegen.annotations.VertxGen
public interface VirtualNoAppRowMapper extends io.vertx.sqlclient.templates.RowMapper<VirtualNoApp> {

  @io.vertx.codegen.annotations.GenIgnore
  VirtualNoAppRowMapper INSTANCE = new VirtualNoAppRowMapper() { };

  @io.vertx.codegen.annotations.GenIgnore
  java.util.stream.Collector<io.vertx.sqlclient.Row, ?, java.util.List<VirtualNoApp>> COLLECTOR = java.util.stream.Collectors.mapping(INSTANCE::map, java.util.stream.Collectors.toList());

  @io.vertx.codegen.annotations.GenIgnore
  default VirtualNoApp map(io.vertx.sqlclient.Row row) {
    VirtualNoApp obj = new VirtualNoApp();
    Object val;
    int idx;
    if ((idx = row.getColumnIndex("app_id")) != -1 && (val = row.getString(idx)) != null) {
      obj.setAppId((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("app_key")) != -1 && (val = row.getString(idx)) != null) {
      obj.setAppKey((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("app_name")) != -1 && (val = row.getString(idx)) != null) {
      obj.setAppName((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("created_by")) != -1 && (val = row.getString(idx)) != null) {
      obj.setCreatedBy((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("created_date")) != -1 && (val = row.getLocalDateTime(idx)) != null) {
      obj.setCreatedDate((java.time.LocalDateTime)val);
    }
    if ((idx = row.getColumnIndex("id_virtualno_app_info")) != -1 && (val = row.getString(idx)) != null) {
      obj.setIdVirtualnoAppInfo((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("secret")) != -1 && (val = row.getString(idx)) != null) {
      obj.setSecret((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("updated_by")) != -1 && (val = row.getString(idx)) != null) {
      obj.setUpdatedBy((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("updated_date")) != -1 && (val = row.getLocalDateTime(idx)) != null) {
      obj.setUpdatedDate((java.time.LocalDateTime)val);
    }
    return obj;
  }
}
