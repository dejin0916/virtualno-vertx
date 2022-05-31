package com.lee.virtualno.apiservice;

import com.lee.virtualno.apiservice.api.OpenApiVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenApiMainVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(OpenApiMainVerticle.class);
  @Override
  public void start(Promise<Void> promise) {
    vertx.deployVerticle(new OpenApiVerticle())
      .onSuccess(success -> {
        logger.info("OpenApiVerticle start success");
        promise.complete();
      })
      .onFailure(err -> {
        logger.error("OpenApiVerticle start failed");
        promise.fail(err);
      });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new OpenApiMainVerticle())
      .onSuccess(logger::info)
      .onFailure(err -> logger.error(err.getMessage()));
  }
}
