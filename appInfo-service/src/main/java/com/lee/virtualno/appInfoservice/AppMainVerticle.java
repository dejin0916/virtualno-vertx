package com.lee.virtualno.appInfoservice;

import com.lee.virtualno.appInfoservice.databases.AppInfoDatabaseVerticle;
import com.lee.virtualno.common.config.DataSourceVerticle;
import io.vertx.core.*;

public class AppMainVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> promise) {
    // 此处需要保证db最先启动完成并发布后才启动其他组件
    vertx.deployVerticle(new DataSourceVerticle()).onSuccess(success ->
        vertx.deployVerticle(new AppInfoDatabaseVerticle())
          .flatMap(r -> vertx.deployVerticle("com.lee.virtualno.appInfoservice.api.HttpAppApiVerticle",
            new DeploymentOptions().setInstances(2)))
          .onSuccess(succ -> promise.complete())
          .onFailure(promise::fail))
      .onFailure(promise::fail);
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new AppMainVerticle())
      .onSuccess(System.out::println)
      .onFailure(System.err::println);
  }
}
