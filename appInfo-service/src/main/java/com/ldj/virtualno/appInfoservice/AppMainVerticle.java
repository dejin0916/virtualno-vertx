package com.ldj.virtualno.appInfoservice;

import com.ldj.virtualno.appInfoservice.databases.AppInfoDatabaseVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class AppMainVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> promise) {
    Promise<String> dbPromise = Promise.promise();
    vertx.deployVerticle(new AppInfoDatabaseVerticle(), dbPromise);

    dbPromise.future().compose(id -> {
      Promise<String> httpVerticleDeployment = Promise.promise();
      vertx.deployVerticle(
        "com.ldj.virtualno.appInfoservice.api.HttpAppApiVerticle",
        new DeploymentOptions().setInstances(2),
        httpVerticleDeployment);
      return httpVerticleDeployment.future();
    }).onSuccess(success -> promise.complete())
      .onFailure(promise::fail);
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new AppMainVerticle())
      .onSuccess(System.out::println)
      .onFailure(System.err::println);
  }
}
