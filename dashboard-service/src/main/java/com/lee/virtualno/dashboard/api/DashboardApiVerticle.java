package com.lee.virtualno.dashboard.api;

import com.lee.virtualno.common.MicroServiceVerticle;
import com.lee.virtualno.common.pojo.ReturnResult;
import com.lee.virtualno.dashboard.databases.VirtualNoPoolDataService;
import com.lee.virtualno.dashboard.databases.VirtualNoPoolNumberDataService;
import com.lee.virtualno.dashboard.entity.VirtualNoPool;
import com.lee.virtualno.dashboard.entity.VirtualNoPoolNumber;
import com.lee.virtualno.dashboard.util.CryptoHelper;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DashboardApiVerticle extends MicroServiceVerticle {

  private static final int HTTP_PORT = 4000;
  private static final Logger logger = LoggerFactory.getLogger(DashboardApiVerticle.class);
  private VirtualNoPoolDataService virtualNoPoolDataService;
  private VirtualNoPoolNumberDataService numberDataService;

  private JWTAuth jwtAuth;
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

    virtualNoPoolDataService = VirtualNoPoolDataService.create(sqlQueries, pgPool);
    numberDataService = VirtualNoPoolNumberDataService.create(sqlQueries, pgPool, redisApi);

    String publicKey = null;
    String privateKey = null;
    try {
      publicKey = CryptoHelper.publicKey();
      privateKey = CryptoHelper.privateKey();
    } catch (IOException e) {
      logger.error("get publicKey and privateKey caught error", e);
    }

    jwtAuth = JWTAuth.create(vertx, new JWTAuthOptions()
      .addPubSecKey(new PubSecKeyOptions()
        .setAlgorithm("RS256")
        .setBuffer(publicKey))
      .addPubSecKey(new PubSecKeyOptions()
        .setAlgorithm("RS256")
        .setBuffer(privateKey)));

    Router router = Router.router(vertx);

    Set<String> allowedHeaders = new HashSet<>();
    allowedHeaders.add("x-requested-with");
    allowedHeaders.add("Access-Control-Allow-Origin");
    allowedHeaders.add("origin");
    allowedHeaders.add("Content-Type");
    allowedHeaders.add("accept");
    allowedHeaders.add("Authorization");

    Set<HttpMethod> allowedMethods = new HashSet<>();
    allowedMethods.add(HttpMethod.GET);
    allowedMethods.add(HttpMethod.POST);
    allowedMethods.add(HttpMethod.OPTIONS);
    allowedMethods.add(HttpMethod.PUT);
    allowedMethods.add(HttpMethod.PATCH);

    router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));

    BodyHandler bodyHandler = BodyHandler.create();
    router.post().handler(bodyHandler);
    router.put().handler(bodyHandler);

    String prefix = "/api/v1";
    JWTAuthHandler jwtHandler = JWTAuthHandler.create(jwtAuth);

    // Account
    router.post(prefix + "/register").handler(this::register);
    router.post(prefix + "/token").handler(this::token);
    router.get(prefix + "/user/:username").handler(this::fetchUser);
    router.get(prefix + "/pool/:pageNum/:pageSize").handler(jwtHandler).handler(this::pagePool);
    router.post(prefix + "/pool/").handler(jwtHandler).handler(this::createPool);
    router.get(prefix + "/pool/number/:serialNumber/:pageNum/:pageSize").handler(jwtHandler).handler(this::pagePoolNumbers);
    router.post(prefix + "/pool/number").handler(jwtHandler).handler(this::createPoolNumber);
    router.patch(prefix + "/pool/number").handler(bodyHandler).handler(jwtHandler).handler(this::updatePoolNumber);
    router.get(prefix + "/app/:pageNum/:pageSize").handler(jwtHandler).handler(this::pageApp);
    router.post(prefix + "/app").handler(jwtHandler).handler(this::addApp);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(HTTP_PORT)
      .onSuccess(success -> logger.info("DashboardApiVerticle start success"))
      .onFailure(err -> logger.error("DashboardApiVerticle start failed", err));
  }

  private void register(RoutingContext context) {
    HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", "user"))
      .onSuccess(webClient -> webClient
        .post("/register")
        .putHeader("Content-Type", "application/json")
        .sendJson(context.getBodyAsJson())
        .onSuccess(resp -> context.response().setStatusCode(resp.statusCode()).end())
        .onFailure(err  -> {
          logger.error("register caught error", err);
          context.fail(502);
        }))
      .onFailure(err -> logger.error("look up user api service failed", err));
  }

  private void token(RoutingContext context) {
    JsonObject payload = context.getBodyAsJson();
    String username = payload.getString("username");
    // 服务发现获取user模块的api
    HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", "user"))
      .onSuccess(webClient -> webClient.post("/authenticate")
        .expect(ResponsePredicate.SC_SUCCESS)
        .sendJson(payload)
          .onSuccess(resp -> {
            JsonObject claims = new JsonObject()
              .put("username", username);
            JWTOptions jwtOptions = new JWTOptions()
              .setAlgorithm("RS256")
              .setExpiresInMinutes(8 * 60)
              .setIssuer("virtual-number")
              .setSubject(username);
            String token = jwtAuth.generateToken(claims, jwtOptions);
            context.response().putHeader("Content-Type", "application/jwt").end(token);
        }).onFailure(err -> {
          logger.error("Authentication error", err);
          context.response().setStatusCode(401).end("Authentication error");
        }))
      .onFailure(err -> logger.error("look up user api service failed", err));
  }

  private void fetchUser(RoutingContext context) {
    String username = context.pathParam("username");
    logger.info("username: {}", username);
    // todo 升级到4.3.0后改为用Web Client URI templates传参
    HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", "user"))
      .onSuccess(webClient -> webClient
        .get("/" + username)
        .send()
        .onSuccess(resp -> {
          logger.info(String.valueOf(resp.statusCode()));
          context.response().putHeader("Content-Type", "application/json").end(resp.bodyAsString());
        }).onFailure(err -> {
          logger.error("Authentication error", err);
          context.response().setStatusCode(401).end("Authentication error");
        }))
      .onFailure(err -> logger.error("look up user api service failed", err));
  }

  private void pagePool(RoutingContext context) {
    int pageNum = Integer.parseInt(context.pathParam("pageNum"));
    int pageSize = Integer.parseInt(context.pathParam("pageSize"));
    virtualNoPoolDataService.fetchPoolsByPage(pageNum, pageSize)
      .onSuccess(resp -> context.response().putHeader("Content-Type", "application/json").end(ReturnResult.success(resp)))
      .onFailure(err -> context.response().setStatusCode(500).end(ReturnResult.failed("page pools failed")));
  }

  private void createPool(RoutingContext context) {
    VirtualNoPool params = context.getBodyAsJson().mapTo(VirtualNoPool.class);
    virtualNoPoolDataService.createPool(params)
      .onSuccess(resp -> context.response().putHeader("Content-Type", "application/json").end(ReturnResult.success()))
      .onFailure(err -> context.response().setStatusCode(500).end(ReturnResult.failed("create pool failed")));
  }

  private void pagePoolNumbers(RoutingContext context) {
    String serialNumber = context.pathParam("serialNumber");
    int pageNum = Integer.parseInt(context.pathParam("pageNum"));
    int pageSize = Integer.parseInt(context.pathParam("pageSize"));
    numberDataService.fetchNumbersByPage(serialNumber, pageNum, pageSize)
      .onSuccess(resp -> context.response().putHeader("Content-Type", "application/json").end(ReturnResult.success(resp)))
      .onFailure(err -> context.response().setStatusCode(500).end(ReturnResult.failed("page virtual numbers failed")));
  }

  private void createPoolNumber(RoutingContext context) {
    VirtualNoPoolNumber poolNumber = context.getBodyAsJson().mapTo(VirtualNoPoolNumber.class);
    numberDataService.createPoolNumber(poolNumber)
      .onSuccess(resp -> context.response().putHeader("Content-Type", "application/json").end(ReturnResult.success()))
      .onFailure(err -> context.response().setStatusCode(500).end(ReturnResult.failed("create virtual number failed")));
  }

  private void updatePoolNumber(RoutingContext context) {
    VirtualNoPoolNumber poolNumber = context.getBodyAsJson().mapTo(VirtualNoPoolNumber.class);
    numberDataService.updatePoolNumber(poolNumber)
      .onSuccess(resp -> context.response().putHeader("Content-Type", "application/json").end(ReturnResult.success()))
      .onFailure(err -> context.response().setStatusCode(500).end(ReturnResult.failed("update virtual number failed")));
  }

  private void pageApp(RoutingContext context) {
    int pageNum = Integer.parseInt(context.pathParam("pageNum"));
    int pageSize = Integer.parseInt(context.pathParam("pageSize"));
    HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", "app"))
      .onSuccess(webClient -> webClient.get("/app/" + pageNum + "/" + pageSize)
        .expect(ResponsePredicate.SC_SUCCESS)
        .send()
        .onSuccess(resp -> context.response().putHeader("Content-Type", "application/json").end(resp.bodyAsString()))
        .onFailure(err -> {
          logger.error(err.getMessage());
          context.response().setStatusCode(500)
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .end(ReturnResult.failed("get app info failed"));
        }))
      .onFailure(err -> logger.error("look up app service failed"));
  }

  private void addApp(RoutingContext context) {
    JsonObject app = context.getBodyAsJson();
    HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", "app"))
      .onSuccess(webClient ->
        webClient.post("/app")
        .expect(ResponsePredicate.SC_SUCCESS)
        .sendJson(app)
        .onSuccess(resp -> context.response().putHeader("Content-Type", "application/json").end(ReturnResult.success()))
        .onFailure(err -> {
          logger.error("add app failed", err);
          context.response().setStatusCode(500)
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .end(ReturnResult.failed("add app failed"));
        }))
      .onFailure(err -> logger.error("look up app service failed"));
  }
}
