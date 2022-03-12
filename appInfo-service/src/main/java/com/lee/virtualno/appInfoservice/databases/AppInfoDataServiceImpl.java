package com.lee.virtualno.appInfoservice.databases;

import com.lee.virtualno.appInfoservice.entity.VirtualNoApp;
import com.lee.virtualno.appInfoservice.entity.VirtualNoAppParametersMapper;
import com.lee.virtualno.appInfoservice.entity.VirtualNoAppRowMapper;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AppInfoDataServiceImpl implements AppInfoDataService {
  private static final Logger logger = LoggerFactory.getLogger(AppInfoDataServiceImpl.class);
  private final HashMap<SqlQuery, String> sqlQueries;
  private final PgPool pgPool;


  public AppInfoDataServiceImpl(HashMap<SqlQuery, String> sqlQueries, PgPool pgPool) {
    this.sqlQueries = sqlQueries;
    this.pgPool = pgPool;
    pgPool.getConnection()
      .onSuccess(success -> logger.info("app info service connect database success"))
      .onFailure(err -> logger.error("app info service connect database failed", err));
  }

  @Override
  public Future<List<VirtualNoApp>> fetchAllApps() {
    Promise<List<VirtualNoApp>> result = Promise.promise();
    SqlTemplate.forQuery(pgPool, sqlQueries.get(SqlQuery.ALL_APPS))
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
  public Future<VirtualNoApp> fetchAppByAppId(String appId) {
    Promise<VirtualNoApp> result = Promise.promise();
    logger.info(sqlQueries.get(SqlQuery.GET_APP_BY_APP_ID));
    SqlTemplate.forQuery(pgPool, sqlQueries.get(SqlQuery.GET_APP_BY_APP_ID))
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
    SqlTemplate.forUpdate(pgPool, sqlQueries.get(SqlQuery.CREATE_APP))
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
    SqlTemplate.forUpdate(pgPool, sqlQueries.get(SqlQuery.SAVE_APP))
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
    SqlTemplate.forUpdate(pgPool, sqlQueries.get(SqlQuery.DELETE_APP))
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
