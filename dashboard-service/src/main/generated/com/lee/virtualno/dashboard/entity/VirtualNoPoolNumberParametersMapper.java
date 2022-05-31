package com.lee.virtualno.dashboard.entity;

/**
 * Mapper for {@link VirtualNoPoolNumber}.
 * NOTE: This class has been automatically generated from the {@link VirtualNoPoolNumber} original class using Vert.x codegen.
 */
@io.vertx.codegen.annotations.VertxGen
public interface VirtualNoPoolNumberParametersMapper extends io.vertx.sqlclient.templates.TupleMapper<VirtualNoPoolNumber> {

  VirtualNoPoolNumberParametersMapper INSTANCE = new VirtualNoPoolNumberParametersMapper() {};

  default io.vertx.sqlclient.Tuple map(java.util.function.Function<Integer, String> mapping, int size, VirtualNoPoolNumber params) {
    java.util.Map<String, Object> args = map(params);
    Object[] array = new Object[size];
    for (int i = 0;i < array.length;i++) {
      String column = mapping.apply(i);
      array[i] = args.get(column);
    }
    return io.vertx.sqlclient.Tuple.wrap(array);
  }

  default java.util.Map<String, Object> map(VirtualNoPoolNumber obj) {
    java.util.Map<String, Object> params = new java.util.HashMap<>();
    params.put("areaCode", obj.getAreaCode());
    params.put("createdBy", obj.getCreatedBy());
    params.put("createdDate", obj.getCreatedDate());
    params.put("enable", obj.isEnable());
    params.put("serialNumber", obj.getSerialNumber());
    params.put("updatedBy", obj.getUpdatedBy());
    params.put("updatedDate", obj.getUpdatedDate());
    params.put("virtualNumber", obj.getVirtualNumber());
    params.put("virtualType", obj.getVirtualType());
    return params;
  }
}
