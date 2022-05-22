/*
* Copyright 2014 Red Hat, Inc.
*
* Red Hat licenses this file to you under the Apache License, version 2.0
* (the "License"); you may not use this file except in compliance with the
* License. You may obtain a copy of the License at:
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/

package com.lee.virtualno.appInfoservice.databases;

import com.lee.virtualno.appInfoservice.databases.AppInfoDataService;
import com.lee.virtualno.common.codec.PageResultCodec;
import io.vertx.core.Vertx;
import io.vertx.core.Handler;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import io.vertx.serviceproxy.ProxyHandler;
import io.vertx.serviceproxy.ServiceException;
import io.vertx.serviceproxy.ServiceExceptionMessageCodec;
import io.vertx.serviceproxy.HelperUtils;
import io.vertx.serviceproxy.ServiceBinder;

import java.util.List;
import com.lee.virtualno.appInfoservice.entity.VirtualNoApp;
import com.lee.virtualno.common.database.PageResult;
import io.vertx.core.Future;
/*
  Generated Proxy code - DO NOT EDIT
  @author Roger the Robot
*/

@SuppressWarnings({"unchecked", "rawtypes"})
public class AppInfoDataServiceVertxProxyHandler extends ProxyHandler {

  public static final long DEFAULT_CONNECTION_TIMEOUT = 5 * 60; // 5 minutes
  private final Vertx vertx;
  private final AppInfoDataService service;
  private final long timerID;
  private long lastAccessed;
  private final long timeoutSeconds;
  private final boolean includeDebugInfo;

  public AppInfoDataServiceVertxProxyHandler(Vertx vertx, AppInfoDataService service){
    this(vertx, service, DEFAULT_CONNECTION_TIMEOUT);
  }

  public AppInfoDataServiceVertxProxyHandler(Vertx vertx, AppInfoDataService service, long timeoutInSecond){
    this(vertx, service, true, timeoutInSecond);
  }

  public AppInfoDataServiceVertxProxyHandler(Vertx vertx, AppInfoDataService service, boolean topLevel, long timeoutInSecond){
    this(vertx, service, true, timeoutInSecond, false);
  }

  public AppInfoDataServiceVertxProxyHandler(Vertx vertx, AppInfoDataService service, boolean topLevel, long timeoutSeconds, boolean includeDebugInfo) {
      this.vertx = vertx;
      this.service = service;
      this.includeDebugInfo = includeDebugInfo;
      this.timeoutSeconds = timeoutSeconds;
      try {
        this.vertx.eventBus().registerDefaultCodec(ServiceException.class,
            new ServiceExceptionMessageCodec());
        // 手动注册自定义codec
        this.vertx.eventBus().registerCodec(new PageResultCodec());
      } catch (IllegalStateException ex) {}
      if (timeoutSeconds != -1 && !topLevel) {
        long period = timeoutSeconds * 1000 / 2;
        if (period > 10000) {
          period = 10000;
        }
        this.timerID = vertx.setPeriodic(period, this::checkTimedOut);
      } else {
        this.timerID = -1;
      }
      accessed();
    }


  private void checkTimedOut(long id) {
    long now = System.nanoTime();
    if (now - lastAccessed > timeoutSeconds * 1000000000) {
      close();
    }
  }

    @Override
    public void close() {
      if (timerID != -1) {
        vertx.cancelTimer(timerID);
      }
      super.close();
    }

    private void accessed() {
      this.lastAccessed = System.nanoTime();
    }

  public void handle(Message<JsonObject> msg) {
    try{
      JsonObject json = msg.body();
      String action = msg.headers().get("action");
      if (action == null) throw new IllegalStateException("action not specified");
      accessed();
      switch (action) {
        case "fetchAllApps": {
          service.fetchAllApps().onComplete(res -> {
                        if (res.failed()) {
                          HelperUtils.manageFailure(msg, res.cause(), includeDebugInfo);
                        } else {
                          msg.reply(new JsonArray(res.result().stream().map(v -> v != null ? v.toJson() : null).collect(Collectors.toList())));
                        }
                     });
          break;
        }
        case "pageAllApps": {
          service.pageAllApps(json.getValue("pageNum") == null ? null : (json.getLong("pageNum").intValue()),
                        json.getValue("pageSize") == null ? null : (json.getLong("pageSize").intValue())).onComplete(res -> {
            if (res.failed()) {
              HelperUtils.manageFailure(msg, res.cause(), includeDebugInfo);
            } else {
              // 此处手动设置自定义codec
              msg.reply(res.result() != null ? res.result().toJson() : null, new DeliveryOptions().setCodecName("PageResultCodec"));
            }
          });
          break;
        }
        case "fetchAppByAppId": {
          service.fetchAppByAppId((java.lang.String)json.getValue("appId")).onComplete(res -> {
                        if (res.failed()) {
                          HelperUtils.manageFailure(msg, res.cause(), includeDebugInfo);
                        } else {
                          msg.reply(res.result() != null ? res.result().toJson() : null);
                        }
                     });
          break;
        }
        case "createApp": {
          service.createApp(json.getJsonObject("virtualNoApp") != null ? new com.lee.virtualno.appInfoservice.entity.VirtualNoApp((JsonObject)json.getJsonObject("virtualNoApp")) : null).onComplete(HelperUtils.createHandler(msg, includeDebugInfo));
          break;
        }
        case "saveApp": {
          service.saveApp((java.lang.String)json.getValue("appId"),
                        (java.lang.String)json.getValue("appKey"),
                        (java.lang.String)json.getValue("secret")).onComplete(HelperUtils.createHandler(msg, includeDebugInfo));
          break;
        }
        case "deleteApp": {
          service.deleteApp((java.lang.String)json.getValue("appId")).onComplete(HelperUtils.createHandler(msg, includeDebugInfo));
          break;
        }
        default: throw new IllegalStateException("Invalid action: " + action);
      }
    } catch (Throwable t) {
      if (includeDebugInfo) msg.reply(new ServiceException(500, t.getMessage(), HelperUtils.generateDebugInfo(t)));
      else msg.reply(new ServiceException(500, t.getMessage()));
      throw t;
    }
  }
}
