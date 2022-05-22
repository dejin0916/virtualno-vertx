package com.lee.virtualno.appInfoservice;

import com.lee.virtualno.appInfoservice.databases.AppInfoDatabaseVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppMainVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(AppMainVerticle.class);
  @Override
  public void start(Promise<Void> promise) {
    // 此处需要保证db最先启动完成并发布后才启动其他组件
    vertx.deployVerticle(new AppInfoDatabaseVerticle())
      .onSuccess(r -> vertx.deployVerticle("com.lee.virtualno.appInfoservice.api.HttpAppApiVerticle",
        new DeploymentOptions().setInstances(2)))
        .onSuccess(succ -> promise.complete())
      .onFailure(promise::fail);
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new AppMainVerticle())
      .onSuccess(logger::info)
      .onFailure(err -> logger.error(err.getMessage()));
  }
}
