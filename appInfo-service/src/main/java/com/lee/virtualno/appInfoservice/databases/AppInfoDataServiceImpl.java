package com.lee.virtualno.appInfoservice.databases;

import com.lee.virtualno.appInfoservice.entity.VirtualNoApp;
import com.lee.virtualno.appInfoservice.entity.VirtualNoAppParametersMapper;
import com.lee.virtualno.appInfoservice.entity.VirtualNoAppRowMapper;
import com.lee.virtualno.common.database.PageResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AppInfoDataServiceImpl implements AppInfoDataService {
  private static final Logger logger = LoggerFactory.getLogger(AppInfoDataServiceImpl.class);
  private final HashMap<String, String> sqlQueries;
  private final PgPool pgPool;


  public AppInfoDataServiceImpl(HashMap<String, String> sqlQueries, PgPool pgPool) {
    this.sqlQueries = sqlQueries;
    this.pgPool = pgPool;
    pgPool.getConnection()
      .onSuccess(success -> logger.info("app info service connect database success"))
      .onFailure(err -> logger.error("app info service connect database failed", err));
  }

  @Override
  public Future<List<VirtualNoApp>> fetchAllApps() {
    Promise<List<VirtualNoApp>> result = Promise.promise();
    SqlTemplate.forQuery(pgPool, sqlQueries.get("all-apps"))
      .mapTo(VirtualNoAppRowMapper.INSTANCE)
      .execute(null)
      .onSuccess(apps -> {
        List<VirtualNoApp> appInfos = new ArrayList<>();
        apps.forEach(appInfos::add);
        result.complete(appInfos);
      })
      .onFailure(err -> {
        logger.error("fetch all apps caught error", err);
        result.fail(err);
      });
    return result.future();
  }

  @Override
  public Future<PageResult<VirtualNoApp>> pageAllApps(int pageNum, int pageSize) {
    Map<String, Object> params = new HashMap<>();
    params.put("limit", pageSize);
    params.put("offset",pageNum > 0 ? (pageNum - 1) * pageSize : 0);
    Promise<PageResult<VirtualNoApp>> result = Promise.promise();
    SqlTemplate.forQuery(pgPool, sqlQueries.get("count-page-apps"))
      .mapTo(Row::toJson)
      .execute(params).onSuccess(row -> {
        int count = row.iterator().next().getInteger("count");
        if(count <= 0) {
          result.complete(new PageResult<VirtualNoApp>(0, pageNum, pageSize).setData(new ArrayList<>()));
        } else {
          SqlTemplate.forQuery(pgPool, sqlQueries.get("page-apps"))
            .mapTo(VirtualNoAppRowMapper.INSTANCE)
            .execute(params)
            .onSuccess(rows -> {
              logger.info("page apps success");
              List<VirtualNoApp> numbers = new ArrayList<>();
              rows.forEach(numbers::add);
              result.complete(new PageResult<VirtualNoApp>(count, pageNum, pageSize).setData(numbers));
            })
            .onFailure(err -> {
              logger.error("page apps failed", err);
              result.fail(err);
            });
        }
      })
      .onFailure(err -> {
        logger.error("count apps failed", err);
        result.fail(err);
      });
    return result.future();
  }

  @Override
  public Future<VirtualNoApp> fetchAppByAppId(String appId) {
    Promise<VirtualNoApp> result = Promise.promise();
    SqlTemplate.forQuery(pgPool, sqlQueries.get("get-app"))
      .mapTo(VirtualNoAppRowMapper.INSTANCE)
      .execute(Collections.singletonMap("appId", appId))
      .onSuccess(apps -> result.complete(apps.iterator().next()))
      .onFailure(err -> {
        logger.error("fetch all apps caught error", err);
        result.fail(err);
      });
    return result.future();
  }

  @Override
  public Future<Void> createApp(VirtualNoApp virtualNoApp) {
    Promise<Void> result = Promise.promise();
    SqlTemplate.forUpdate(pgPool, sqlQueries.get("create-app"))
      .mapFrom(VirtualNoAppParametersMapper.INSTANCE)
      .execute(virtualNoApp)
      .onSuccess(success -> {
        logger.info("create app success");
        result.complete();
      })
      .onFailure(err -> {
        logger.error("create app caught error", err);
        result.fail(err);
      });
    return result.future();
  }

  @Override
  public Future<Void> saveApp(String appId, String appKey, String secret) {
    Promise<Void> result = Promise.promise();
    Map<String, Object> updateParams = new HashMap<>();
    updateParams.put("appKey", appKey);
    updateParams.put("secret", secret);
    updateParams.put("appId", appId);
    SqlTemplate.forUpdate(pgPool, sqlQueries.get("save-app"))
      .execute(updateParams)
      .onSuccess(success -> {
        logger.info("update app success");
        result.complete();
      })
      .onFailure(err -> {
        logger.error("update app caught error", err);
        result.fail(err);
      });
    return result.future();
  }

  @Override
  public Future<Void> deleteApp(String appId) {
    Promise<Void> result = Promise.promise();
    SqlTemplate.forUpdate(pgPool, sqlQueries.get("delete-app"))
      .execute(Collections.singletonMap("appId", appId))
      .onSuccess(success -> {
        logger.info("delete app success");
        result.complete();
      })
      .onFailure(err -> {
        logger.error("delete app caught error", err);
        result.fail(err);
      });
    return result.future();
  }
}
