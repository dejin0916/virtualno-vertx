package com.lee.virtualno.user;


import com.lee.virtualno.common.MicroServiceVerticle;
import com.lee.virtualno.common.config.DataSourceVerticle;
import com.lee.virtualno.user.api.HttpUserApiVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class UserMainVerticle extends MicroServiceVerticle {
  @Override
  public void start(Promise<Void> promise) throws Exception {
    vertx.deployVerticle(new DataSourceVerticle()).onSuccess(success ->
        vertx.deployVerticle(new HttpUserApiVerticle())
          .onSuccess(suc -> promise.complete())
          .onFailure(promise::fail))
      .onFailure(promise::fail);
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new UserMainVerticle())
      .onSuccess(System.out::println)
      .onFailure(System.err::println);
  }
}
