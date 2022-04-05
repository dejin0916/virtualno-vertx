package com.lee.virtualno.user.api;

import com.lee.virtualno.common.MicroServiceVerticle;
import com.lee.virtualno.common.discovery.PgPoolDataSource;
import com.lee.virtualno.user.databases.VirtualNoUserDataService;
import com.lee.virtualno.user.entity.VirtualNoUser;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.HashingStrategy;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.sqlclient.SqlAuthentication;
import io.vertx.ext.auth.sqlclient.SqlAuthenticationOptions;
import io.vertx.ext.auth.sqlclient.SqlUserUtil;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static io.vertx.ext.auth.impl.Codec.base64Encode;

/**
 *
 */
public class HttpUserApiVerticle extends MicroServiceVerticle {
  private static final Logger logger = LoggerFactory.getLogger(HttpUserApiVerticle.class);

  private PgPool pgPool;
  private AuthenticationProvider authProvider;
  private SqlUserUtil userUtil;
  private VirtualNoUserDataService virtualNoUserDataService;
  private final SecureRandom random = new SecureRandom();

  @Override
  public void start(Promise<Void> promise) throws Exception {
    super.start();
    PgPoolDataSource.getPgPool(discovery, record -> record.getName().equals("virtualno-pgpool"))
      .onSuccess(pool -> {
        logger.info("look up pg pool success");
        pgPool = pool;
      })
      .onFailure(err -> {
        logger.error("look up pg pool caught error", err);
        promise.fail(err);
      });

    virtualNoUserDataService = VirtualNoUserDataService.create(sqlQueries, pgPool);

    authProvider = SqlAuthentication.create(pgPool,
      new SqlAuthenticationOptions().setAuthenticationQuery(sqlQueries.get("authentication-sql")));
    userUtil = SqlUserUtil.create(pgPool, sqlQueries.get("register-user"), null, null);

    Router router = Router.router(vertx);
    BodyHandler bodyHandler = BodyHandler.create();
    router.post().handler(bodyHandler);
    router.put().handler(bodyHandler);
    router.post("/register").handler(this::register);
    router.get("/:username").handler(this::fetchUser);
    router.put("/:username").handler(this::updateUser);
    router.post("/authenticate").handler(this::authenticate);
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(9999)
      .onSuccess(success -> logger.info("user api expose on port 9999"))
      .onFailure(err -> logger.error("user api expose failed", err));
  }

  private void register(RoutingContext context) {
    JsonObject body = context.getBodyAsJson();
    String username = body.getString("username");
    String password = body.getString("password");
    String nickname = body.getString("nickname");
    String contact = body.getString("contact");
    userUtil.createUser(username, password)
      .onSuccess(success -> {
        logger.info("create user {} success", username);
        populateInfo(username, nickname, contact, context);
      })
      .onFailure(err -> logger.error("create user {} failed", username, err));
  }

  private void populateInfo(String username, String nickname, String contact, RoutingContext context) {
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("username", username);
    paramMap.put("nickname", nickname);
    paramMap.put("contact", contact);
    SqlTemplate.forUpdate(pgPool, sqlQueries.get("populate-user-info")).execute(paramMap)
      .onSuccess(success -> {
        logger.info("populate user info success");
        context.response()
          .putHeader("content-type", "application/text")
          .setStatusCode(200)
          .end("success");
      })
      .onFailure(err -> {
        logger.error("populate user info failed", err);
        context.response().setStatusCode(500).end("failed");
      });
  }

  private void fetchUser(RoutingContext context) {
    String username = context.pathParam("username");
    virtualNoUserDataService.fetchUser(username)
      .onSuccess(user -> context.response()
        .putHeader("content-type", "application/text")
        .setStatusCode(200).end(user.toJson().encode()))
      .onFailure(err -> context.response().setStatusCode(500).end("fetch user failed"));
  }

  private void updateUser(RoutingContext context) {
    HashingStrategy strategy = HashingStrategy.load();
    final byte[] salt = new byte[32];
    random.nextBytes(salt);
    VirtualNoUser body = context.getBodyAsJson().mapTo(VirtualNoUser.class);
    String password = body.getPassword();
    String hash = strategy.hash("pbkdf2", null, base64Encode(salt), password);
    body.setPassword(hash);
    String username = context.pathParam("username");
    body.setUsername(username);
    body.setUpdatedDate(LocalDateTime.now());
    virtualNoUserDataService.updateUser(body)
      .onSuccess(success -> context.response().putHeader("content-type", "application/text")
        .setStatusCode(200).end("success"))
      .onFailure(err -> context.response().setStatusCode(500).end("update user failed"));
  }

  private void authenticate(RoutingContext context) {
    authProvider.authenticate(context.getBodyAsJson())
      .onSuccess(success -> {
        logger.info("authentication success");
        context.response()
          .putHeader("content-type", "application/text")
          .setStatusCode(200).end("authenticate success");
      })
      .onFailure(err -> {
        logger.error("authentication problem {}", err.getMessage());
        context.response()
          .putHeader("content-type", "application/text")
          .setStatusCode(401).end("authenticate failed");
      });
  }
}
