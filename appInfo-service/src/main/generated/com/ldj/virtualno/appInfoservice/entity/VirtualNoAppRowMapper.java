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
    if ((idx = row.getColumnIndex("appId")) != -1 && (val = row.getString(idx)) != null) {
      obj.setAppId((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("appKey")) != -1 && (val = row.getString(idx)) != null) {
      obj.setAppKey((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("appName")) != -1 && (val = row.getString(idx)) != null) {
      obj.setAppName((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("id")) != -1 && (val = row.getString(idx)) != null) {
      obj.setId((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("secret")) != -1 && (val = row.getString(idx)) != null) {
      obj.setSecret((java.lang.String)val);
    }
    return obj;
  }
}
