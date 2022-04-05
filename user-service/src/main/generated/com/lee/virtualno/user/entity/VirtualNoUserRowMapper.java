package com.lee.virtualno.user.entity;

/**
 * Mapper for {@link VirtualNoUser}.
 * NOTE: This class has been automatically generated from the {@link VirtualNoUser} original class using Vert.x codegen.
 */
@io.vertx.codegen.annotations.VertxGen
public interface VirtualNoUserRowMapper extends io.vertx.sqlclient.templates.RowMapper<VirtualNoUser> {

  @io.vertx.codegen.annotations.GenIgnore
  VirtualNoUserRowMapper INSTANCE = new VirtualNoUserRowMapper() { };

  @io.vertx.codegen.annotations.GenIgnore
  java.util.stream.Collector<io.vertx.sqlclient.Row, ?, java.util.List<VirtualNoUser>> COLLECTOR = java.util.stream.Collectors.mapping(INSTANCE::map, java.util.stream.Collectors.toList());

  @io.vertx.codegen.annotations.GenIgnore
  default VirtualNoUser map(io.vertx.sqlclient.Row row) {
    VirtualNoUser obj = new VirtualNoUser();
    Object val;
    int idx;
    if ((idx = row.getColumnIndex("contact")) != -1 && (val = row.getString(idx)) != null) {
      obj.setContact((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("created_by")) != -1 && (val = row.getString(idx)) != null) {
      obj.setCreatedBy((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("created_date")) != -1 && (val = row.getLocalDateTime(idx)) != null) {
      obj.setCreatedDate((java.time.LocalDateTime)val);
    }
    if ((idx = row.getColumnIndex("nickname")) != -1 && (val = row.getString(idx)) != null) {
      obj.setNickname((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("password")) != -1 && (val = row.getString(idx)) != null) {
      obj.setPassword((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("updated_by")) != -1 && (val = row.getString(idx)) != null) {
      obj.setUpdatedBy((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("updated_date")) != -1 && (val = row.getLocalDateTime(idx)) != null) {
      obj.setUpdatedDate((java.time.LocalDateTime)val);
    }
    if ((idx = row.getColumnIndex("username")) != -1 && (val = row.getString(idx)) != null) {
      obj.setUsername((java.lang.String)val);
    }
    return obj;
  }
}
