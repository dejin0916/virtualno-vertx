package com.lee.virtualno.appInfoservice.databases;

import com.lee.virtualno.common.MicroServiceVerticle;
import com.lee.virtualno.common.discovery.PgPoolDataSource;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class AppInfoDatabaseVerticle extends MicroServiceVerticle {
  private static final Logger logger = LoggerFactory.getLogger(AppInfoDatabaseVerticle.class);

  public static final String CONFIG_APP_SQL_QUERIES_RESOURCE_FILE = "app.sqlqueries.resource.file";
  public static final String CONFIG_APP_QUEUE = "app.queue";
  private PgPool pgPool;
  @Override
  public void start(Promise<Void> promise) throws Exception {
    super.start();
    HashMap<SqlQuery, String> sqlQueries = loadSqlQueries();
    PgPoolDataSource.getPgPool(discovery, record -> record.getName().equals("virtualno-pgpool"))
      .onSuccess(pool -> {
        logger.info("look up pg pool success");
        pgPool = pool;
      })
      .onFailure(err -> {
        logger.error("look up pg pool caught error", err);
        promise.fail(err);
      });

    ServiceBinder binder = new ServiceBinder(vertx);
    binder.setAddress(CONFIG_APP_QUEUE).register(AppInfoDataService.class, AppInfoDataService.create(sqlQueries, pgPool));
    logger.info("AppInfoDatabaseVerticle start successful");
    promise.complete();
  }

  private HashMap<SqlQuery, String> loadSqlQueries() throws IOException {
    String queriesFile = config().getString(CONFIG_APP_SQL_QUERIES_RESOURCE_FILE);
    InputStream queriesInputStream;
    if (queriesFile != null) {
      queriesInputStream = new FileInputStream(queriesFile);
    } else {
      queriesInputStream = getClass().getResourceAsStream("/sql.properties");
    }

    Properties queriesProps = new Properties();
    queriesProps.load(queriesInputStream);
    assert queriesInputStream != null;
    queriesInputStream.close();

    HashMap<SqlQuery, String> sqlQueries = new HashMap<>();
    sqlQueries.put(SqlQuery.ALL_APPS, queriesProps.getProperty("all-apps"));
    sqlQueries.put(SqlQuery.GET_APP_BY_APP_ID, queriesProps.getProperty("get-app"));
    sqlQueries.put(SqlQuery.CREATE_APP, queriesProps.getProperty("create-app"));
    sqlQueries.put(SqlQuery.SAVE_APP, queriesProps.getProperty("save-app"));
    sqlQueries.put(SqlQuery.DELETE_APP, queriesProps.getProperty("delete-app"));
    return sqlQueries;
  }

  @Override
  public void stop(Promise<Void> promise) throws Exception {

  }
}
