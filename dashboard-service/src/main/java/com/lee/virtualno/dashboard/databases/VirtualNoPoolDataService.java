package com.lee.virtualno.dashboard.databases;

import com.lee.virtualno.common.database.PageResult;
import com.lee.virtualno.dashboard.entity.VirtualNoPool;
import io.vertx.core.Future;
import io.vertx.pgclient.PgPool;

import java.util.HashMap;

public interface VirtualNoPoolDataService {

  Future<Void> createPool(VirtualNoPool pool);

  Future<VirtualNoPool> fetchPool(String serialNumber);

  Future<PageResult<VirtualNoPool>> fetchPoolsByPage(int pageNum, int pageSize);

  Future<Void> updatePool(VirtualNoPool params);

  static VirtualNoPoolDataService create(HashMap<String, String> sqlQueries, PgPool pgPool) {
    return new VirtualNoPoolDataServiceImpl(sqlQueries, pgPool);
  }
}
