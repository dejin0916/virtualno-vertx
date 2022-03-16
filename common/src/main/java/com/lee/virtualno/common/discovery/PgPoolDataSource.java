package com.lee.virtualno.common.discovery;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.spi.ServiceType;

import java.util.Objects;
import java.util.function.Function;

/**
 * 主要多个verticle使用pgPool之前都要写重复的PgPool pgPool = PgPool.pool(.....)
 * 自定义pgPoolDatasource然后发布，需要的verticle再去服务发现中拿
 */
public interface PgPoolDataSource extends ServiceType {
  String TYPE = "pgPool";


  static Record createRecord(String name, JsonObject location, JsonObject metadata) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(location);

    Record record = new Record().setName(name)
      .setType(TYPE)
      .setLocation(location);

    if (metadata != null) {
      record.setMetadata(metadata);
    }

    return record;
  }

  /**
   * Convenient method that looks for a pgPool datasource source and provides the configured {@link io.vertx.pgclient.PgPool}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter, optional
   * @param resultHandler The result handler
   */
  static void getPgPool(ServiceDiscovery discovery, JsonObject filter,
                            Handler<AsyncResult<PgPool>> resultHandler) {
    getPgPool(discovery, filter).onComplete(resultHandler);
  }

  /**
   * Like {@link #getPgPool(ServiceDiscovery, JsonObject, Handler)} but returns a future of the result
   */
  static Future<PgPool> getPgPool(ServiceDiscovery discovery, JsonObject filter) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReference(res).get());
      }
    });
  }

  /**
   * Convenient method that looks for a pgPool datasource source and provides the configured {@link io.vertx.pgclient.PgPool}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter (must not be {@code null})
   * @param resultHandler The result handler
   */
  static void getPgPool(ServiceDiscovery discovery, Function<Record, Boolean> filter,
                            Handler<AsyncResult<PgPool>> resultHandler) {
    getPgPool(discovery, filter).onComplete(resultHandler);
  }

  /**
   * Like {@link #getPgPool(ServiceDiscovery, Function, Handler)} but returns a future of the result
   */
  static Future<PgPool> getPgPool(ServiceDiscovery discovery, Function<Record, Boolean> filter) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReference(res).get());
      }
    });
  }

  /**
   * Convenient method that looks for a pgPool datasource source and provides the configured {@link io.vertx.pgclient.PgPool}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery             The service discovery instance
   * @param filter                The filter, optional
   * @param consumerConfiguration the consumer configuration
   * @param resultHandler         the result handler
   */
  static void getPgPool(ServiceDiscovery discovery, JsonObject filter, JsonObject consumerConfiguration,
                            Handler<AsyncResult<PgPool>> resultHandler) {
    getPgPool(discovery, filter, consumerConfiguration).onComplete(resultHandler);
  }

  /**
   * Like {@link #getPgPool(ServiceDiscovery, JsonObject, JsonObject, Handler)} but returns a future of the result
   */
  static Future<PgPool> getPgPool(ServiceDiscovery discovery, JsonObject filter, JsonObject consumerConfiguration) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReferenceWithConfiguration(res, consumerConfiguration).get());
      }
    });
  }

  /**
   * Convenient method that looks for a pgPool datasource source and provides the configured {@link io.vertx.pgclient.PgPool}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery             The service discovery instance
   * @param filter                The filter, must not be {@code null}
   * @param consumerConfiguration the consumer configuration
   * @param resultHandler         the result handler
   */
  static void getPgPool(ServiceDiscovery discovery, Function<Record, Boolean> filter, JsonObject consumerConfiguration,
                            Handler<AsyncResult<PgPool>> resultHandler) {
    getPgPool(discovery, filter, consumerConfiguration).onComplete(resultHandler);
  }

  /**
   * Like {@link #getPgPool(ServiceDiscovery, Function, JsonObject, Handler)} but returns a future of the result
   */
  static Future<PgPool> getPgPool(ServiceDiscovery discovery, Function<Record, Boolean> filter, JsonObject consumerConfiguration) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReferenceWithConfiguration(res, consumerConfiguration).get());
      }
    });
  }
}
