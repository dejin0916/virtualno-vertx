package com.ldj.virtualno.appInfoservice.databases;

import com.ldj.virtualno.appInfoservice.entity.VirtualNoApp;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    pgPool.preparedQuery(sqlQueries.get(SqlQuery.ALL_APPS)).execute()
      .onSuccess(rows -> {
        List<VirtualNoApp> apps = new ArrayList<>();
        for (Row row : rows) {
          apps.add(new JsonObject()
            .put("id", row.getString("id_virtualno_app_info"))
            .put("appId",row.getString("app_id"))
            .put("appName",row.getString("app_name"))
            .put("appKey",row.getString("app_key"))
            .put("secret",row.getString("secret")).mapTo(VirtualNoApp.class)
          );
        }
        result.complete(apps);
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
    pgPool.preparedQuery(sqlQueries.get(SqlQuery.GET_APP_BY_APP_ID)).execute(Tuple.of(appId))
      .onSuccess(rows -> {
        VirtualNoApp virtualNoApp = new VirtualNoApp();
        if (rows.size() == 0) {
          result.complete(virtualNoApp);
        } else {
          for (Row row : rows) {
            virtualNoApp = new JsonObject().put("id", row.getString("id_virtualno_app_info"))
              .put("appId",row.getString("app_id"))
              .put("appName",row.getString("app_name"))
              .put("appKey",row.getString("app_key"))
              .put("secret",row.getString("secret")).mapTo(VirtualNoApp.class);
            break;
          }
          result.complete(virtualNoApp);
        }
      }).onFailure(err -> {
        logger.error("fetch app by appId error", err);
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
