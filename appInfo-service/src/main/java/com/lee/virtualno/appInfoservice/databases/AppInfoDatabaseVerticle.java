package com.lee.virtualno.appInfoservice.databases;

import com.lee.virtualno.common.MicroServiceVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.serviceproxy.ServiceBinder;
import io.vertx.sqlclient.PoolOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 尝试下将vertx的service proxy以及将db的交互代码跑在多个verticel上
 */
public class AppInfoDatabaseVerticle extends MicroServiceVerticle {
  private static final Logger logger = LoggerFactory.getLogger(AppInfoDatabaseVerticle.class);

  public static final String CONFIG_APP_QUEUE = "app.queue";

  @Override
  public void start(Promise<Void> promise) throws Exception {
    super.start();
    JsonObject pgConfig = new JsonObject().put("host","localhost")
      .put("port", 5432).put("database", "postgres").put("user", "postgres")
      .put("password", "postgres");
    JsonObject poolConfig = new JsonObject()
      .put("maxSize", 16).put("maxWaitQueueSize", 64).put("idleTimeout", 0).put("idleTimeoutUnit", TimeUnit.SECONDS)
      .put("shared", true).put("name", "virtualno-pool").put("eventLoopSize",4);

    PgPool pgPool = PgPool.pool(vertx, new PgConnectOptions(pgConfig), new PoolOptions(poolConfig));

    ServiceBinder binder = new ServiceBinder(vertx);
    binder.setAddress(CONFIG_APP_QUEUE).register(AppInfoDataService.class, AppInfoDataService.create(sqlQueries, pgPool));
    logger.info("AppInfoDatabaseVerticle start successful");
    promise.complete();
  }
}
