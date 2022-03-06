package com.ldj.virtualno.appInfoservice.api;

import com.ldj.virtualno.appInfoservice.databases.AppInfoDataService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpAppApiVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(HttpAppApiVerticle.class);

  private static final String CONFIG_APP_QUEUE = "app.queue";

  private AppInfoDataService appDataService;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    String appQueue = config().getString(CONFIG_APP_QUEUE,"app.queue");
    appDataService = AppInfoDataService.createProxy(vertx, appQueue);


    Router router = Router.router(vertx);
    BodyHandler bodyHandler = BodyHandler.create();
    router.post().handler(bodyHandler);
    router.put().handler(bodyHandler);

    router.get("/").handler(this::home);
    router.get("/apps").handler(this::fetchAllApplication);
    router.post("/app").handler(this::addApplication);
    router.get("/apps/:appId").handler(this::fetchApplication);
    router.put("/app").handler(this::updateApplication);
    router.delete("/app/:appId").handler(this::deleteApplication);

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
  }

  private void deleteApplication(RoutingContext context) {
  }
}
