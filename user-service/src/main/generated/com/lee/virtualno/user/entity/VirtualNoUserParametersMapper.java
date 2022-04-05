package com.lee.virtualno.user.entity;

/**
 * Mapper for {@link VirtualNoUser}.
 * NOTE: This class has been automatically generated from the {@link VirtualNoUser} original class using Vert.x codegen.
 */
@io.vertx.codegen.annotations.VertxGen
public interface VirtualNoUserParametersMapper extends io.vertx.sqlclient.templates.TupleMapper<VirtualNoUser> {

  VirtualNoUserParametersMapper INSTANCE = new VirtualNoUserParametersMapper() {};

  default io.vertx.sqlclient.Tuple map(java.util.function.Function<Integer, String> mapping, int size, VirtualNoUser params) {
    java.util.Map<String, Object> args = map(params);
    Object[] array = new Object[size];
    for (int i = 0;i < array.length;i++) {
      String column = mapping.apply(i);
      array[i] = args.get(column);
    }
    return io.vertx.sqlclient.Tuple.wrap(array);
  }

  default java.util.Map<String, Object> map(VirtualNoUser obj) {
    java.util.Map<String, Object> params = new java.util.HashMap<>();
    params.put("contact", obj.getContact());
    params.put("createdBy", obj.getCreatedBy());
    params.put("createdDate", obj.getCreatedDate());
    params.put("nickname", obj.getNickname());
    params.put("password", obj.getPassword());
    params.put("updatedBy", obj.getUpdatedBy());
    params.put("updatedDate", obj.getUpdatedDate());
    params.put("username", obj.getUsername());
    return params;
  }
}
