package com.lee.virtualno.common.config;

import com.lee.virtualno.common.MicroServiceVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class DataSourceVerticle extends MicroServiceVerticle {
  Logger logger = LoggerFactory.getLogger(DataSourceVerticle.class);
  @Override
  public void start() throws Exception {
    super.start();
    JsonObject pgConfig = new JsonObject().put("host","localhost")
        .put("port", 5432).put("database", "virtualno").put("user", "postgres")
        .put("password", "postgres");
    JsonObject poolConfig = new JsonObject()
      .put("maxSize", 16).put("maxWaitQueueSize", -1).put("idleTimeout", 0).put("idleTimeoutUnit", TimeUnit.SECONDS)
      .put("shared", true).put("name", "virtualno_pool").put("eventLoopSize",4);

    publishPgPoolDataSource("virtualno-pgpool", pgConfig, poolConfig)
      .onSuccess(success -> logger.info("publish PgPool success"))
      .onFailure(err -> logger.error("publish PgPool caught error", err));
  }

  @Override
  public void stop(Promise<Void> promise) throws Exception {
    // do nothing
  }
}
