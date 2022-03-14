package com.lee.virtualno.appInfoservice.api;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lee.virtualno.appInfoservice.databases.AppInfoDataService;
import com.lee.virtualno.appInfoservice.entity.VirtualNoApp;
import com.lee.virtualno.common.MicroServiceVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpAppApiVerticle extends MicroServiceVerticle {
  private static final Logger logger = LoggerFactory.getLogger(HttpAppApiVerticle.class);

  private static final String CONFIG_APP_QUEUE = "app.queue";

  private AppInfoDataService appDataService;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();

    String appQueue = config().getString(CONFIG_APP_QUEUE,"app.queue");
    appDataService = AppInfoDataService.createProxy(vertx, appQueue);


    Router router = Router.router(vertx);
    BodyHandler bodyHandler = BodyHandler.create();
    router.post().handler(bodyHandler);
    router.patch().handler(bodyHandler);

    router.get("/").handler(this::home);
    router.get("/app").handler(this::fetchAllApplication);
    router.post("/app").handler(this::addApplication);
    router.get("/app/:appId").handler(this::fetchApplication);
    router.patch("/app/:appId").handler(this::updateApplication);
    router.delete("/app/:appId").handler(this::deleteApplication);

    publishHttpEndpoint("app", "localhost", config().getInteger("http.port", 8888))
      .onSuccess(success -> logger.info("AppInfoService (App endpoint) service published"))
      .onFailure(Throwable::printStackTrace);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8888)
      .onSuccess(suc -> logger.info("app-service start success at port 8888"))
      .onFailure(err -> logger.error("app-service start failed", err));
  }

  private void home(RoutingContext context) {
    context.response().end("hello");
  }


  private void fetchAllApplication(RoutingContext context) {
    appDataService.fetchAllApps()
      .onSuccess(success -> context.response().setStatusCode(200).putHeader("content-type", "application/json")
        .end(Json.encodePrettily(success)))
      .onFailure(err -> context.response().setStatusCode(404).end());
  }


  private void addApplication(RoutingContext context) {
    appDataService.createApp(context.getBodyAsJson().mapTo(VirtualNoApp.class))
      .onSuccess(success -> context.response().setStatusCode(200)
        .putHeader("content-type", "application/json").end("add app success"))
      .onFailure(err -> context.response().setStatusCode(500).end(err.getMessage()));
  }

  private void fetchApplication(RoutingContext context) {
    String appId = context.pathParam("appId");
    logger.info("appId is {}", appId);
    appDataService.fetchAppByAppId(appId)
      .onSuccess(success -> context.response().setStatusCode(200).putHeader("content-type", "application/json")
        .end(Json.encodePrettily(success)))
      .onFailure(err -> context.response().setStatusCode(404).end());
  }

  private void updateApplication(RoutingContext context) {
    JsonObject body = context.getBodyAsJson();
    String appId = context.pathParam("appId");
    String appKey = body.getString("appKey");
    String secret = body.getString("secret");
    appDataService.saveApp(appId, appKey, secret)
      .onSuccess(success -> context.response().setStatusCode(200)
        .putHeader("content-type", "application/json").end("update success success"))
      .onFailure(err -> context.response().setStatusCode(500).end(err.getMessage()));
  }

  private void deleteApplication(RoutingContext context) {
    appDataService.deleteApp(context.pathParam("appId"))
      .onSuccess(success -> context.response().setStatusCode(200)
        .putHeader("content-type", "application/json").end("delete app success"))
      .onFailure(err -> context.response().setStatusCode(500).end(err.getMessage()));
  }
}
