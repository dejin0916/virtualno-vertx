package com.ldj.virtualno.appInfoservice.databases;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ldj.virtualno.appInfoservice.entity.VirtualNoApp;
import com.ldj.virtualno.appInfoservice.entity.VirtualNoAppRowMapper;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AppInfoDataServiceImpl implements AppInfoDataService {
  private static final Logger logger = LoggerFactory.getLogger(AppInfoDataServiceImpl.class);
  private final HashMap<SqlQuery, String> sqlQueries;
  private final PgPool pgPool;


  public AppInfoDataServiceImpl(HashMap<SqlQuery, String> sqlQueries, PgPool pgPool) {
    DatabindCodec.mapper().registerModule(new JavaTimeModule());
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
    return null;
  }

  @Override
  public Future<Void> saveApp(String appKey, String secret) {
    return null;
  }

  @Override
  public Future<Void> deleteApp(String id) {
    return null;
  }
}
