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

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.Vertx;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

import java.util.List;
import java.util.stream.Collectors;

import io.vertx.serviceproxy.ServiceException;
import io.vertx.serviceproxy.ServiceExceptionMessageCodec;

import com.lee.virtualno.appInfoservice.entity.VirtualNoApp;
/*
  Generated Proxy code - DO NOT EDIT
  @author Roger the Robot
*/

@SuppressWarnings({"unchecked", "rawtypes"})
public class AppInfoDataServiceVertxEBProxy implements AppInfoDataService {
  private Vertx _vertx;
  private String _address;
  private DeliveryOptions _options;
  private boolean closed;

  public AppInfoDataServiceVertxEBProxy(Vertx vertx, String address) {
    this(vertx, address, null);
  }

  public AppInfoDataServiceVertxEBProxy(Vertx vertx, String address, DeliveryOptions options) {
    this._vertx = vertx;
    this._address = address;
    this._options = options;
    try {
      this._vertx.eventBus().registerDefaultCodec(ServiceException.class, new ServiceExceptionMessageCodec());
    } catch (IllegalStateException ex) {
    }
  }

  @Override
  public Future<List<VirtualNoApp>> fetchAllApps(){
    if (closed) return io.vertx.core.Future.failedFuture("Proxy is closed");
    JsonObject _json = new JsonObject();

    DeliveryOptions _deliveryOptions = (_options != null) ? new DeliveryOptions(_options) : new DeliveryOptions();
    _deliveryOptions.addHeader("action", "fetchAllApps");
    return _vertx.eventBus().<JsonArray>request(_address, _json, _deliveryOptions).map(msg -> {
      return msg.body().stream()
        .map(v -> v != null ? new VirtualNoApp((JsonObject)v) : null)
        .collect(Collectors.toList());
    });
  }
  @Override
  public Future<VirtualNoApp> fetchAppByAppId(String appId){
    if (closed) return io.vertx.core.Future.failedFuture("Proxy is closed");
    JsonObject _json = new JsonObject();
    _json.put("appId", appId);

    DeliveryOptions _deliveryOptions = (_options != null) ? new DeliveryOptions(_options) : new DeliveryOptions();
    _deliveryOptions.addHeader("action", "fetchAppByAppId");
    return _vertx.eventBus().<JsonObject>request(_address, _json, _deliveryOptions).map(msg -> {
      return msg.body() != null ? new VirtualNoApp((JsonObject)msg.body()) : null;
    });
  }
  @Override
  public Future<Void> createApp(VirtualNoApp virtualNoApp){
    if (closed) return io.vertx.core.Future.failedFuture("Proxy is closed");
    JsonObject _json = new JsonObject();
    _json.put("virtualNoApp", virtualNoApp != null ? virtualNoApp.toJson() : null);

    DeliveryOptions _deliveryOptions = (_options != null) ? new DeliveryOptions(_options) : new DeliveryOptions();
    _deliveryOptions.addHeader("action", "createApp");
    return _vertx.eventBus().<Void>request(_address, _json, _deliveryOptions).map(msg -> {
      return msg.body();
    });
  }
  @Override
  public Future<Void> saveApp(String appId, String appKey, String secret){
    if (closed) return io.vertx.core.Future.failedFuture("Proxy is closed");
    JsonObject _json = new JsonObject();
    _json.put("appId", appId);
    _json.put("appKey", appKey);
    _json.put("secret", secret);

    DeliveryOptions _deliveryOptions = (_options != null) ? new DeliveryOptions(_options) : new DeliveryOptions();
    _deliveryOptions.addHeader("action", "saveApp");
    return _vertx.eventBus().<Void>request(_address, _json, _deliveryOptions).map(msg -> {
      return msg.body();
    });
  }
  @Override
  public Future<Void> deleteApp(String appId){
    if (closed) return io.vertx.core.Future.failedFuture("Proxy is closed");
    JsonObject _json = new JsonObject();
    _json.put("appId", appId);

    DeliveryOptions _deliveryOptions = (_options != null) ? new DeliveryOptions(_options) : new DeliveryOptions();
    _deliveryOptions.addHeader("action", "deleteApp");
    return _vertx.eventBus().<Void>request(_address, _json, _deliveryOptions).map(msg -> {
      return msg.body();
    });
  }
}
