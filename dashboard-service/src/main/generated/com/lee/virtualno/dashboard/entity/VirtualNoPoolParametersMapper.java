package com.lee.virtualno.dashboard.entity;

/**
 * Mapper for {@link VirtualNoPool}.
 * NOTE: This class has been automatically generated from the {@link VirtualNoPool} original class using Vert.x codegen.
 */
@io.vertx.codegen.annotations.VertxGen
public interface VirtualNoPoolParametersMapper extends io.vertx.sqlclient.templates.TupleMapper<VirtualNoPool> {

  VirtualNoPoolParametersMapper INSTANCE = new VirtualNoPoolParametersMapper() {};

  default io.vertx.sqlclient.Tuple map(java.util.function.Function<Integer, String> mapping, int size, VirtualNoPool params) {
    java.util.Map<String, Object> args = map(params);
    Object[] array = new Object[size];
    for (int i = 0;i < array.length;i++) {
      String column = mapping.apply(i);
      array[i] = args.get(column);
    }
    return io.vertx.sqlclient.Tuple.wrap(array);
  }

  default java.util.Map<String, Object> map(VirtualNoPool obj) {
    java.util.Map<String, Object> params = new java.util.HashMap<>();
    params.put("createdBy", obj.getCreatedBy());
    params.put("createdDate", obj.getCreatedDate());
    params.put("description", obj.getDescription());
    params.put("enable", obj.isEnable());
    params.put("serialNumber", obj.getSerialNumber());
    params.put("updatedBy", obj.getUpdatedBy());
    params.put("updatedDate", obj.getUpdatedDate());
    return params;
  }
}
