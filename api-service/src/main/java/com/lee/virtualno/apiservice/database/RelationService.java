package com.lee.virtualno.apiservice.database;

import com.lee.virtualno.apiservice.entity.VirtualnoAxRel;
import com.lee.virtualno.apiservice.entity.VirtualnoAxbRel;
import com.lee.virtualno.apiservice.request.AxBindRequest;
import com.lee.virtualno.apiservice.request.AxbBindRequest;
import io.vertx.core.Future;
import io.vertx.pgclient.PgPool;
import io.vertx.redis.client.RedisAPI;

import java.util.HashMap;

public interface RelationService {

  Future<VirtualnoAxRel> bindAx(AxBindRequest request);

  Future<VirtualnoAxbRel> bindAxb(AxbBindRequest request);

  static RelationService create(HashMap<String, String> sqlQueries, PgPool pgPool, RedisAPI redisApi) {
    return new RelationServiceImpl(sqlQueries, pgPool, redisApi);
  }
}
