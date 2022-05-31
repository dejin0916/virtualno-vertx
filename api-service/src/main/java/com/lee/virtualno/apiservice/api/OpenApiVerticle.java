package com.lee.virtualno.apiservice.api;

import com.lee.virtualno.apiservice.database.RelationService;
import com.lee.virtualno.apiservice.handler.SignVerifyHandler;
import com.lee.virtualno.apiservice.request.AxBindRequest;
import com.lee.virtualno.common.MicroServiceVerticle;
import com.lee.virtualno.common.pojo.ReturnResult;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.HashingStrategy;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisClientType;
import io.vertx.redis.client.RedisOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.sqlclient.PoolOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.auth.impl.Codec.base64Encode;

public class OpenApiVerticle extends MicroServiceVerticle {
  private static final int HTTP_PORT = 6666;
  private static final Logger logger = LoggerFactory.getLogger(OpenApiVerticle.class);
  private RelationService relationService;
  private final HashingStrategy strategy = HashingStrategy.load();
  private final SecureRandom random = new SecureRandom();


  @Override
  public void start() throws Exception {
    super.start();
    JsonObject pgConfig = new JsonObject().put("host","localhost")
      .put("port", 5432).put("database", "postgres").put("user", "postgres")
      .put("password", "postgres");
    JsonObject poolConfig = new JsonObject()
      .put("maxSize", 16).put("maxWaitQueueSize", 64).put("idleTimeout", 0).put("idleTimeoutUnit", TimeUnit.SECONDS)
      .put("shared", true).put("name", "virtualno-pool").put("eventLoopSize",4);

    PgPool pgPool = PgPool.pool(vertx, new PgConnectOptions(pgConfig), new PoolOptions(poolConfig));
    RedisOptions redisOptions = new RedisOptions()
      .setType(RedisClientType.STANDALONE)
      .addConnectionString("redis://localhost:6379")
      .setMaxPoolSize(4)
      .setMaxPoolWaiting(16);
    Redis redisClient = Redis.createClient(vertx, redisOptions);
    RedisAPI redisApi = RedisAPI.api(redisClient);
    relationService = RelationService.create(sqlQueries, pgPool, redisApi);

    Router router = Router.router(vertx);

    Set<String> allowedHeaders = new HashSet<>();
    allowedHeaders.add("x-requested-with");
    allowedHeaders.add("Access-Control-Allow-Origin");
    allowedHeaders.add("origin");
    allowedHeaders.add("Content-Type");
    allowedHeaders.add("accept");
    allowedHeaders.add("appId");
    allowedHeaders.add("sign");
    allowedHeaders.add("timestamp");

    Set<HttpMethod> allowedMethods = new HashSet<>();
    allowedMethods.add(HttpMethod.GET);
    allowedMethods.add(HttpMethod.POST);
    allowedMethods.add(HttpMethod.PUT);
    allowedMethods.add(HttpMethod.PATCH);

    router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));

    BodyHandler bodyHandler = BodyHandler.create();
    router.post().handler(bodyHandler);
    router.put().handler(bodyHandler);

    String prefix = "/api/v1";
    SignVerifyHandler signVerifyHandler = SignVerifyHandler.create();
    router.get(prefix + "/sign/:appId").handler(this::generateSign);
    router.post(prefix + "/ax/bind").handler(this::bindAx);
    router.post(prefix + "/axb/bind").handler(signVerifyHandler).handler(this::bindAxb);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(HTTP_PORT)
      .onSuccess(success -> logger.info("OpenApiVerticle start success at port {}", HTTP_PORT))
      .onFailure(err -> logger.error("OpenApiVerticle start failed", err));
  }


  private void generateSign(RoutingContext context) {
    String appId = context.pathParam("appId");
    HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", "app"))
      .onSuccess(webClient ->
        webClient.get("/app/" + appId)
          .expect(ResponsePredicate.SC_SUCCESS)
          .send()
          .onSuccess(resp -> {
            final byte[] salt = new byte[32];
            long timestamp = System.currentTimeMillis();
            random.nextBytes(salt);
            JsonObject appInfo = resp.bodyAsJsonObject().getJsonObject("data");
            String sign = strategy.hash("pbkdf2", null, base64Encode(salt), appInfo.getString("appKey") + appInfo.getString("secret") + timestamp);
            JsonObject signInfo = new JsonObject().put("sign", sign).put("timestamp", timestamp);
            context.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
              .end(ReturnResult.success(signInfo));
          })
          .onFailure(err -> {
            logger.error("add app failed", err);
            context.response().setStatusCode(500)
              .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
              .end(ReturnResult.failed("get app info failed"));
          }))
      .onFailure(err -> logger.error("look up app service failed"));
  }

  private void bindAx(RoutingContext context) {
    AxBindRequest request = context.getBodyAsJson().mapTo(AxBindRequest.class);
    request.setAppId(context.request().getHeader("appId"));
    relationService.bindAx(request)
      .onSuccess(resp -> context.response().putHeader("Content-Type", "application/json").end(ReturnResult.success(resp)))
      .onFailure(err -> context.response().setStatusCode(500).end(ReturnResult.failed("bind ax relation failed")));
  }

  private void bindAxb(RoutingContext context) {
  }

}
