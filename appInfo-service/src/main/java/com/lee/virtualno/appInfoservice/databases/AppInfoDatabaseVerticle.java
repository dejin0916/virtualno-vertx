package com.lee.virtualno.appInfoservice.databases;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.serviceproxy.ServiceBinder;
import io.vertx.sqlclient.PoolOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class AppInfoDatabaseVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(AppInfoDatabaseVerticle.class);

  public static final String CONFIG_APP_SQL_QUERIES_RESOURCE_FILE = "app.sqlqueries.resource.file";
  public static final String CONFIG_APP_QUEUE = "app.queue";

  @Override
  public void start(Promise<Void> promise) throws Exception {
    DatabindCodec.mapper().registerModule(new JavaTimeModule());
    DatabindCodec.prettyMapper().registerModule(new JavaTimeModule());
    HashMap<SqlQuery, String> sqlQueries = loadSqlQueries();
    PgPool pgPool = PgPool.pool(vertx,
        new PgConnectOptions().setHost("localhost")
          .setPort(5432)
          .setDatabase("postgres")
          .setUser("postgres")
          .setPassword("postgres"), new PoolOptions());

    ServiceBinder binder = new ServiceBinder(vertx);
    binder.setAddress(CONFIG_APP_QUEUE).register(AppInfoDataService.class, AppInfoDataService.create(sqlQueries, pgPool));
    promise.complete();
    logger.info("AppInfoDatabaseVerticle start successful");
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
}
