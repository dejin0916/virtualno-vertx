package com.lee.virtualno.common.discovery;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.AbstractServiceReference;
import io.vertx.sqlclient.PoolOptions;

import java.util.Objects;

public class PgPoolDataSourceImpl implements PgPoolDataSource {
  @Override
  public String name() {
    return PgPoolDataSource.TYPE;
  }

  @Override
  public ServiceReference get(Vertx vertx, ServiceDiscovery discovery, Record record, JsonObject configuration) {
    Objects.requireNonNull(vertx);
    Objects.requireNonNull(record);
    Objects.requireNonNull(discovery);
    return new PgPoolServiceReference(vertx, discovery, record, configuration);
  }

  private static class PgPoolServiceReference extends AbstractServiceReference<PgPool> {
    private final JsonObject config;

    PgPoolServiceReference(Vertx vertx, ServiceDiscovery discovery, Record record, JsonObject config) {
      super(vertx, discovery, record);
      this.config = config;
    }

    @Override
    public PgPool retrieve() {
      JsonObject result = record().getMetadata().copy();
      result.mergeIn(record().getLocation());

      if (config != null) {
        result.mergeIn(config);
      }
      return PgPool.pool(vertx, new PgConnectOptions(result), new PoolOptions(result));
    }

    @Override
    protected void onClose() {
      service.close();
    }
  }
}
