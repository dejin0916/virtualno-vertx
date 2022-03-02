package com.ldj.virtualno.appInfoservice.entity;

/**
 * Mapper for {@link VirtualNoApp}.
 * NOTE: This class has been automatically generated from the {@link VirtualNoApp} original class using Vert.x codegen.
 */
@io.vertx.codegen.annotations.VertxGen
public interface VirtualNoAppParametersMapper extends io.vertx.sqlclient.templates.TupleMapper<VirtualNoApp> {

  VirtualNoAppParametersMapper INSTANCE = new VirtualNoAppParametersMapper() {};

  default io.vertx.sqlclient.Tuple map(java.util.function.Function<Integer, String> mapping, int size, VirtualNoApp params) {
    java.util.Map<String, Object> args = map(params);
    Object[] array = new Object[size];
    for (int i = 0;i < array.length;i++) {
      String column = mapping.apply(i);
      array[i] = args.get(column);
    }
    return io.vertx.sqlclient.Tuple.wrap(array);
  }

  default java.util.Map<String, Object> map(VirtualNoApp obj) {
    java.util.Map<String, Object> params = new java.util.HashMap<>();
    params.put("appId", obj.getAppId());
    params.put("appKey", obj.getAppKey());
    params.put("appName", obj.getAppName());
    params.put("id", obj.getId());
    params.put("secret", obj.getSecret());
    return params;
  }
}
