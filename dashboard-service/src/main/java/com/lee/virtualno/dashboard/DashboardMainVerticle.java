package com.lee.virtualno.dashboard;

import com.lee.virtualno.dashboard.api.DashboardApiVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardMainVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(DashboardMainVerticle.class);
  @Override
  public void start(Promise<Void> promise) throws Exception {
    vertx.deployVerticle(new DashboardApiVerticle())
      .onSuccess(success -> {
        logger.info("DashboardApiVerticle start success");
        promise.complete();
      })
      .onFailure(err -> {
        logger.error("DashboardApiVerticle start failed");
        promise.fail(err);
      });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new DashboardMainVerticle())
      .onSuccess(logger::info)
      .onFailure(err -> logger.error(err.getMessage()));
  }
}
