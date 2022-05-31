package com.lee.virtualno.dashboard.databases;

import com.lee.virtualno.common.constant.CommConstant;
import com.lee.virtualno.common.database.PageResult;
import com.lee.virtualno.dashboard.entity.VirtualNoPoolNumber;
import com.lee.virtualno.dashboard.entity.VirtualNoPoolNumberParametersMapper;
import com.lee.virtualno.dashboard.entity.VirtualNoPoolNumberRowMapper;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.pgclient.PgPool;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class VirtualNoPoolNumberDataServiceImpl implements VirtualNoPoolNumberDataService {

  private static final Logger logger = LoggerFactory.getLogger(VirtualNoPoolNumberDataServiceImpl.class);
  private final HashMap<String, String> sqlQueries;
  private final PgPool pgPool;
  private final RedisAPI redisApi;

  public VirtualNoPoolNumberDataServiceImpl(HashMap<String, String> sqlQueries, PgPool pgPool, RedisAPI redisApi) {
    this.sqlQueries = sqlQueries;
    this.pgPool = pgPool;
    this.redisApi = redisApi;
  }

  @Override
  public Future<Void> createPoolNumber(VirtualNoPoolNumber poolNumber) {
    Promise<Void> result = Promise.promise();
    SqlTemplate.forUpdate(pgPool, sqlQueries.get("create-pool-number"))
      .mapFrom(VirtualNoPoolNumberParametersMapper.INSTANCE)
      .execute(poolNumber)
      .onSuccess(success -> {
        redisApi.zadd(Arrays.asList(
          String.format(CommConstant.VN_BIND_TIMES_POOL, poolNumber.getSerialNumber()),
          poolNumber.getVirtualNumber(), "0"))
          .flatMap(response -> {
            if(CommConstant.TYPE_AX.equals(poolNumber.getVirtualType())) {
              return redisApi.zadd(Arrays.asList(
                String.format(CommConstant.VN_DIALED_TIMES_POOL, poolNumber.getSerialNumber()),
                poolNumber.getVirtualNumber(), "0"));
            }
            return null;
          });
        logger.info("create virtualno success");
        result.complete();
      })
      .onFailure(err -> {
        logger.error("create virtualno caught error", err);
        result.fail(err);
      });
    return result.future();
  }


  @Override
  public Future<PageResult<VirtualNoPoolNumber>> fetchNumbersByPage(String serialNumber, int pageNum, int pageSize) {
    Map<String, Object> params = new HashMap<>();
    params.put("limit", pageSize);
    params.put("offset",pageNum > 0 ? (pageNum - 1) * pageSize : 0);
    params.put("serialNumber", serialNumber);
    Promise<PageResult<VirtualNoPoolNumber>> result = Promise.promise();
    SqlTemplate.forQuery(pgPool, sqlQueries.get("count-page-pool-numbers"))
      .mapTo(Row::toJson)
      .execute(params).onSuccess(row -> {
        int count = row.iterator().next().getInteger("count");
        if(count <= 0) {
          result.complete(new PageResult<VirtualNoPoolNumber>(0, pageNum, pageSize).setData(new ArrayList<>()));
        } else {
          SqlTemplate.forQuery(pgPool, sqlQueries.get("page-pool-numbers"))
            .mapTo(VirtualNoPoolNumberRowMapper.INSTANCE)
            .execute(params)
            .onSuccess(rows -> {
              logger.info("page virtual numbers success");
              List<VirtualNoPoolNumber> numbers = new ArrayList<>();
              rows.forEach(numbers::add);
              result.complete(new PageResult<VirtualNoPoolNumber>(count, pageNum, pageSize).setData(numbers));
            })
            .onFailure(err -> {
              logger.error("page virtual numbers failed", err);
              result.fail(err);
            });
        }
      })
      .onFailure(err -> {
        logger.error("count virtual numbers failed", err);
        result.fail(err);
      });
    return result.future();
  }

  @Override
  public Future<Void> updatePoolNumber(VirtualNoPoolNumber number) {
    Promise<Void> result = Promise.promise();
    logger.info(sqlQueries.get("save-pool-number"));
    SqlTemplate.forUpdate(pgPool, sqlQueries.get("save-pool-number"))
      .mapFrom(VirtualNoPoolNumberParametersMapper.INSTANCE)
      .execute(number)
      .onSuccess(success -> {
        logger.info("update virtual number success");
        result.complete();
      })
      .onFailure(err -> {
        logger.error("update virtual number caught error", err);
        result.fail(err);
      });
    return result.future();
  }
}
