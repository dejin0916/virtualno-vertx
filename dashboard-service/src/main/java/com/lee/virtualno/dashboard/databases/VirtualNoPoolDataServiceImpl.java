package com.lee.virtualno.dashboard.databases;

import com.lee.virtualno.common.database.PageResult;
import com.lee.virtualno.dashboard.entity.VirtualNoPool;
import com.lee.virtualno.dashboard.entity.VirtualNoPoolParametersMapper;
import com.lee.virtualno.dashboard.entity.VirtualNoPoolRowMapper;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class VirtualNoPoolDataServiceImpl implements VirtualNoPoolDataService {

  private static final Logger logger = LoggerFactory.getLogger(VirtualNoPoolDataServiceImpl.class);
  private final HashMap<String, String> sqlQueries;
  private final PgPool pgPool;


  public VirtualNoPoolDataServiceImpl(HashMap<String, String> sqlQueries, PgPool pgPool) {
    this.sqlQueries = sqlQueries;
    this.pgPool = pgPool;
  }

  @Override
  public Future<Void> createPool(VirtualNoPool pool) {
    Promise<Void> result = Promise.promise();
    SqlTemplate.forUpdate(pgPool, sqlQueries.get("create-pool"))
      .mapFrom(VirtualNoPoolParametersMapper.INSTANCE)
      .execute(pool)
      .onSuccess(success -> {
        logger.info("create virtualno pool success");
        result.complete();
      })
      .onFailure(err -> {
        logger.error("create virtualno pool caught error", err);
        result.fail(err);
      });
    return result.future();
  }

  @Override
  public Future<VirtualNoPool> fetchPool(String serialNumber) {
    Promise<VirtualNoPool> result = Promise.promise();
    SqlTemplate.forUpdate(pgPool, sqlQueries.get("get-pool"))
      .mapTo(VirtualNoPoolRowMapper.INSTANCE)
      .execute(Collections.singletonMap("serialNumber", serialNumber))
      .onSuccess(success -> {
        logger.info("fetch virtualno pool success");
        result.complete();
      })
      .onFailure(err -> {
        logger.error("fetch virtualno pool caught error", err);
        result.fail(err);
      });
    return result.future();
  }

  @Override
  public Future<PageResult<VirtualNoPool>> fetchPoolsByPage(int pageNum, int pageSize) {
    Map<String, Object> params = new HashMap<>();
    params.put("limit", pageSize);
    params.put("offset",pageNum > 0 ? (pageNum - 1) * pageSize : 0);
    Promise<PageResult<VirtualNoPool>> result = Promise.promise();
    SqlTemplate.forQuery(pgPool, sqlQueries.get("count-page-pools"))
      .mapTo(Row::toJson)
      .execute(params).onSuccess(row -> {
        int count = row.iterator().next().getInteger("count");
        if(count <= 0) {
          result.complete(new PageResult<VirtualNoPool>(0, pageNum, pageSize).setData(new ArrayList<>()));
        } else {
          SqlTemplate.forQuery(pgPool, sqlQueries.get("page-pools"))
            .mapTo(VirtualNoPoolRowMapper.INSTANCE)
            .execute(params)
            .onSuccess(rows -> {
              logger.info("page pools success");
              List<VirtualNoPool> pools = new ArrayList<>();
              rows.forEach(pools::add);
              result.complete(new PageResult<VirtualNoPool>(count, pageNum, pageSize).setData(pools));
            })
            .onFailure(err -> {
              logger.error("page pools failed", err);
              result.fail(err);
            });
        }
      })
      .onFailure(err -> {
        logger.error("count pools failed", err);
        result.fail(err);
      });
    return result.future();
  }

  @Override
  public Future<Void> updatePool(VirtualNoPool params) {
    Promise<Void> result = Promise.promise();
    SqlTemplate.forUpdate(pgPool, sqlQueries.get("save-pool"))
      .mapFrom(VirtualNoPoolParametersMapper.INSTANCE)
      .execute(params)
      .onSuccess(success -> {
        logger.info("update pool success");
        result.complete();
      })
      .onFailure(err -> {
        logger.error("update pool caught error", err);
        result.fail(err);
      });
    return result.future();
  }
}
