package com.lee.virtualno.apiservice.database;

import com.lee.virtualno.apiservice.entity.VirtualnoAxRel;
import com.lee.virtualno.apiservice.entity.VirtualnoAxRelParametersMapper;
import com.lee.virtualno.apiservice.entity.VirtualnoAxbRel;
import com.lee.virtualno.apiservice.request.AxBindRequest;
import com.lee.virtualno.apiservice.request.AxbBindRequest;
import com.lee.virtualno.apiservice.util.ScriptReader;
import com.lee.virtualno.common.constant.CommConstant;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;

public class RelationServiceImpl implements RelationService {
  private static final Logger logger = LoggerFactory.getLogger(RelationServiceImpl.class);

  private final HashMap<String, String> sqlQueries;
  private final PgPool pgPool;
  private final RedisAPI redisApi;
  public RelationServiceImpl(HashMap<String, String> sqlQueries, PgPool pgPool, RedisAPI redisApi) {
    this.sqlQueries = sqlQueries;
    this.pgPool = pgPool;
    this.redisApi = redisApi;
  }

  @Override
  public Future<VirtualnoAxRel> bindAx(AxBindRequest request) {
    Promise<VirtualnoAxRel> promise = Promise.promise();
    Random random = new Random(5);
    List<String> args = new ArrayList<>();
    try {
      args.add(ScriptReader.getXForAx());
    } catch (IOException e) {
      promise.fail(e);
    }
    if(CommConstant.MUST_MATCH_AREA.equals(request.getAreaMatchMode())) {
      args.add("1"); // 标注key有1个,vertx-redis-client的eval，key和arg是混在一起的
      args.add(String.format(CommConstant.VN_BIND_TIMES_POOL_WITH_AREA, request.getSerialNumber(), request.getAreaCode()));
    } else {
      args.add("2"); // 标注key有1个
      args.add(String.format(CommConstant.VN_BIND_TIMES_POOL_WITH_AREA, request.getSerialNumber(), request.getAreaCode()));
      args.add(String.format(CommConstant.VN_BIND_TIMES_POOL, request.getSerialNumber()));
    }
    args.add(request.getSerialNumber());
    args.add(String.valueOf(random.nextInt(100)));
    logger.info("args is {}", Json.encodePrettily(args));
    redisApi.eval(args).onSuccess(response -> {
      String virtualNumber = response.toString(Charset.defaultCharset());
      VirtualnoAxRel rel = new VirtualnoAxRel();
      rel.setAppId(request.getAppId());
      rel.setBusinessType(request.getBusinessType());
      rel.setAreaCode(request.getAreaCode());
      rel.setRealNumber(request.getRealNumber());
      rel.setVirtualNumber(virtualNumber);
      rel.setSerialNumber(request.getSerialNumber());
      rel.setUnbindPlanDate(request.getUnbindPlanDate());
      rel.setCreatedBy(request.getAppId());
      rel.setCreatedDate(LocalDateTime.now());
      rel.setUpdatedBy(request.getAppId());
      rel.setUpdatedDate(LocalDateTime.now());
      SqlTemplate.forUpdate(pgPool, sqlQueries.get("create-ax-rel")).mapFrom(VirtualnoAxRelParametersMapper.INSTANCE)
        .execute(rel).onSuccess(success -> {
          promise.complete(rel);
          logger.info("add ax relation success");
        })
        .onFailure(error -> {
          logger.error("add ax relation failed", error);
          promise.fail(error);
        });
    }).onFailure(err -> {
      logger.error("get virtual number from pool failed", err);
      promise.fail(err);
    });
    return promise.future();
  }

  @Override
  public Future<VirtualnoAxbRel> bindAxb(AxbBindRequest request) {
    return null;
  }
}
