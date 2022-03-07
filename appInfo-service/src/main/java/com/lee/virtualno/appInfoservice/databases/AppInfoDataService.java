package com.lee.virtualno.appInfoservice.databases;

import com.lee.virtualno.appInfoservice.entity.VirtualNoApp;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgPool;

import java.util.HashMap;
import java.util.List;

@ProxyGen
public interface AppInfoDataService {

  Future<List<VirtualNoApp>> fetchAllApps();

  Future<VirtualNoApp> fetchAppByAppId(String appId);

  Future<Void> createApp(VirtualNoApp virtualNoApp);

  Future<Void> saveApp(String appId, String appKey, String secret);

  Future<Void> deleteApp(String appId);

  @GenIgnore
  static AppInfoDataService create(HashMap<SqlQuery, String> sqlQueries, PgPool pgPool) {
    return new AppInfoDataServiceImpl(sqlQueries, pgPool);
  }

  @GenIgnore
  static AppInfoDataService createProxy(Vertx vertx, String address) {
    return new AppInfoDataServiceVertxEBProxy(vertx, address);
  }

}
