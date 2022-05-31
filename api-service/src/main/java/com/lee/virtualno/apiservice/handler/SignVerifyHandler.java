package com.lee.virtualno.apiservice.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface SignVerifyHandler extends Handler<RoutingContext> {

  static SignVerifyHandler create() {
    return new SignVerifyHandlerImpl();
  }

}
