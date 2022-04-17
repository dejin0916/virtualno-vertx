package com.ldj.virtualno.apiservice;

import com.ldj.virtualno.apiservice.util.CryptoHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PublicApiVerticle extends AbstractVerticle {

  private static final int HTTP_PORT = 4000;
  private static final Logger logger = LoggerFactory.getLogger(PublicApiVerticle.class);

  private WebClient webClient;
  private JWTAuth jwtAuth;
  @Override
  public void start(Promise<Void> startPromise) {
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

    router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));

    BodyHandler bodyHandler = BodyHandler.create();
    router.post().handler(bodyHandler);
    router.put().handler(bodyHandler);

    String prefix = "/api/v1";
    JWTAuthHandler jwtHandler = JWTAuthHandler.create(jwtAuth);

    // Account
    router.post(prefix + "/register").handler(this::register);
    router.post(prefix + "/token").handler(this::token);
    webClient = WebClient.create(vertx);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(HTTP_PORT)
      .onSuccess(success -> logger.info("publicApiVerticle start success"))
      .onFailure(err -> logger.error("publicApiVerticle start failed", err));
  }

  private void register(RoutingContext context) {
    webClient
      .post(9999, "localhost", "/register")
      .putHeader("Content-Type", "application/json")
      .sendJson(context.getBodyAsJson())
      .onSuccess(resp -> context.response().setStatusCode(resp.statusCode()).end())
      .onFailure(err  -> {
        logger.error("register caught error", err);
        context.fail(502);
      });
  }

  private void token(RoutingContext context) {
    JsonObject payload = context.getBodyAsJson();
    String username = payload.getString("username");
    webClient.post(9999, "localhost", "/authenticate")
      .expect(ResponsePredicate.SC_SUCCESS)
      .sendJson(payload)
      .onSuccess(resp -> {
        JsonObject claims = new JsonObject()
          .put("username", username);
        JWTOptions jwtOptions = new JWTOptions()
          .setAlgorithm("RS256")
          .setExpiresInMinutes(30)
          .setIssuer("virtual-number")
          .setSubject(username);
        String token = jwtAuth.generateToken(claims, jwtOptions);
        context.response().putHeader("Content-Type", "application/jwt").end(token);
      }).onFailure(err -> {
        logger.error("Authentication error", err);
        context.fail(401);
      });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle("com.ldj.virtualno.apiservice.PublicApiVerticle", new DeploymentOptions().setInstances(2))
      .onSuccess(success -> {
        logger.info("PublicApiVerticle start success");
      })
      .onFailure(err -> {
        logger.error("PublicApiVerticle start failed", err);
      });
  }
}
