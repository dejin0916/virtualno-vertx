package com.lee.virtualno.dashboard.databases;

import com.lee.virtualno.common.database.PageResult;
import com.lee.virtualno.dashboard.entity.VirtualNoPool;
import com.lee.virtualno.dashboard.entity.VirtualNoPoolNumber;
import io.vertx.core.Future;
import io.vertx.pgclient.PgPool;
import io.vertx.redis.client.RedisAPI;

import java.util.HashMap;

public interface VirtualNoPoolNumberDataService {

  Future<Void> createPoolNumber(VirtualNoPoolNumber poolNumber);

  Future<PageResult<VirtualNoPoolNumber>> fetchNumbersByPage(String serialNumber, int pageNum, int pageSize);

  Future<Void> updatePoolNumber(VirtualNoPoolNumber poolNumber);

  static VirtualNoPoolNumberDataService create(HashMap<String, String> sqlQueries, PgPool pgPool, RedisAPI redisApi) {
    return new VirtualNoPoolNumberDataServiceImpl(sqlQueries, pgPool, redisApi);
  }
}
