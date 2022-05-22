package com.ldj.virtualno.apiservice.handler;

import com.lee.virtualno.common.pojo.ReturnResult;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.HashingStrategy;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignVerifyHandlerImpl implements SignVerifyHandler {
  private static final Logger logger = LoggerFactory.getLogger(SignVerifyHandlerImpl.class);

  HashingStrategy strategy = HashingStrategy.load();

  @Override
  public void handle(RoutingContext context) {
    HttpServerRequest request = context.request();
    String appId = request.getHeader("appId");
    String sign = request.getHeader("sign");
    String requestTimeStamp = request.getHeader("timestamp");
    if(StringUtils.isEmpty(appId) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(requestTimeStamp)) {
      context.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .end(ReturnResult.failed("appId, sign, timestamp must not be empty"));
      return;
    }

    if(System.currentTimeMillis() - Long.parseLong(requestTimeStamp) > 180000) {
      context.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .end(ReturnResult.failed("The timestamp is not in the valid range"));
      return;
    }

    JsonObject app = getCredentByWebClient(appId, context);
    String secret = app.getString("secret");
    String key = app.getString("appKey");

    if(!strategy.verify(sign, secret + key + requestTimeStamp)) {
      context.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .end(ReturnResult.failed("app verify failed"));
      return;
    }
    context.next();
  }

  private JsonObject getCredentByWebClient(String appId, RoutingContext context) {
    JsonObject app = new JsonObject();
    WebClient webClient = WebClient.create(context.vertx());
    webClient.get(8888, "localhost", "/app/" + appId)
      .expect(ResponsePredicate.SC_SUCCESS)
      .as(BodyCodec.jsonObject())
      .send()
      .onSuccess(result -> result.body().mergeIn(app))
      .onFailure(err -> {
        logger.error(err.getMessage());
        context.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(ReturnResult.failed("get app info failed"));
      });
    return app;
  }
}
