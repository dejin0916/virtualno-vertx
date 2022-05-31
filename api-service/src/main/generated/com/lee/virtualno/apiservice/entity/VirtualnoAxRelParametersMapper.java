package com.lee.virtualno.apiservice.entity;

/**
 * Mapper for {@link VirtualnoAxRel}.
 * NOTE: This class has been automatically generated from the {@link VirtualnoAxRel} original class using Vert.x codegen.
 */
@io.vertx.codegen.annotations.VertxGen
public interface VirtualnoAxRelParametersMapper extends io.vertx.sqlclient.templates.TupleMapper<VirtualnoAxRel> {

  VirtualnoAxRelParametersMapper INSTANCE = new VirtualnoAxRelParametersMapper() {};

  default io.vertx.sqlclient.Tuple map(java.util.function.Function<Integer, String> mapping, int size, VirtualnoAxRel params) {
    java.util.Map<String, Object> args = map(params);
    Object[] array = new Object[size];
    for (int i = 0;i < array.length;i++) {
      String column = mapping.apply(i);
      array[i] = args.get(column);
    }
    return io.vertx.sqlclient.Tuple.wrap(array);
  }

  default java.util.Map<String, Object> map(VirtualnoAxRel obj) {
    java.util.Map<String, Object> params = new java.util.HashMap<>();
    params.put("appId", obj.getAppId());
    params.put("areaCode", obj.getAreaCode());
    params.put("businessId", obj.getBusinessId());
    params.put("businessType", obj.getBusinessType());
    params.put("createdBy", obj.getCreatedBy());
    params.put("createdDate", obj.getCreatedDate());
    params.put("idVirtualnoAx", obj.getIdVirtualnoAx());
    params.put("realNumber", obj.getRealNumber());
    params.put("serialNumber", obj.getSerialNumber());
    params.put("unbindPlanDate", obj.getUnbindPlanDate());
    params.put("unbindRealDate", obj.getUnbindRealDate());
    params.put("updatedBy", obj.getUpdatedBy());
    params.put("updatedDate", obj.getUpdatedDate());
    params.put("virtualNumber", obj.getVirtualNumber());
    return params;
  }
}
