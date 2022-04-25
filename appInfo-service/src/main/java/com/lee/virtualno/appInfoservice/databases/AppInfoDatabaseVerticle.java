package com.lee.virtualno.appInfoservice.databases;

import com.lee.virtualno.common.MicroServiceVerticle;
import com.lee.virtualno.common.discovery.PgPoolDataSource;
import io.vertx.core.Promise;
import io.vertx.pgclient.PgPool;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppInfoDatabaseVerticle extends MicroServiceVerticle {
  private static final Logger logger = LoggerFactory.getLogger(AppInfoDatabaseVerticle.class);

  public static final String CONFIG_APP_QUEUE = "app.queue";
  private PgPool pgPool;
  @Override
  public void start(Promise<Void> promise) throws Exception {
    super.start();
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

  @Override
  public void stop() {
    ServiceDiscovery.releaseServiceObject(discovery, pgPool);
  }
}
