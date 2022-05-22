package com.lee.virtualno.user;


import com.lee.virtualno.user.api.HttpUserApiVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserMainVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(UserMainVerticle.class);
  @Override
  public void start(Promise<Void> promise) throws Exception {
    vertx.deployVerticle(new HttpUserApiVerticle())
      .onSuccess(suc -> {
        logger.info("http service start success");
        promise.complete();
      })
      .onFailure(err -> {
        logger.error("http service start failed", err);
        promise.fail(err);
      });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new UserMainVerticle())
      .onSuccess(logger::info)
      .onFailure(err -> logger.error(err.getMessage()));
  }
}
