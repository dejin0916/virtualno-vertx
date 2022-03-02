package com.lee.virtualno.common;

import io.vertx.core.*;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.types.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MicroServiceVerticle extends AbstractVerticle {
  protected ServiceDiscovery discovery;
  protected Set<io.vertx.servicediscovery.Record> registeredRecords = new ConcurrentHashSet<>();

  @Override
  public void start() throws Exception {
    discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(config()));
  }

  public void publishHttpEndpoint(String name, String host, int port, Handler<AsyncResult<Void>> completionHandler) {
    io.vertx.servicediscovery.Record record = HttpEndpoint.createRecord(name, host, port, "/");
    publish(record, completionHandler);
  }

  public void publishMessageSource(String name, String address, Class<?> contentClass, Handler<AsyncResult<Void>> completionHandler) {
    io.vertx.servicediscovery.Record record = MessageSource.createRecord(name, address, contentClass);
    publish(record, completionHandler);
  }

  public void publishMessageSource(String name, String address, Handler<AsyncResult<Void>> completionHandler) {
    io.vertx.servicediscovery.Record record = MessageSource.createRecord(name, address);
    publish(record, completionHandler);
  }

  public void publishEventBusService(String name, String address, Class<?> serviceClass, Handler<AsyncResult<Void>> completionHandler) {
    io.vertx.servicediscovery.Record record = EventBusService.createRecord(name, address, serviceClass);
    publish(record, completionHandler);
  }

  protected void publish(io.vertx.servicediscovery.Record record, Handler<AsyncResult<Void>> completionHandler) {
    if (discovery == null) {
      try {
        start();
      } catch (Exception e) {
        throw new RuntimeException("Cannot create discovery service");
      }
    }

    discovery.publish(record)
      .onComplete(ar -> {
        if(ar.succeeded()) {
          registeredRecords.add(record);
        }
        completionHandler.handle(ar.map(null));
    });
  }

  @Override
  public void stop(Promise<Void> promise) throws Exception {
    List<Promise<Void>> promises = new ArrayList<>();
    for (io.vertx.servicediscovery.Record record : registeredRecords) {
      Promise<Void> unRegistrationPromise = Promise.promise();
      promises.add(unRegistrationPromise);
      discovery.unpublish(record.getRegistration(), unRegistrationPromise);
    }
    if(promises.isEmpty()) {
      discovery.close();
      promise.complete();
    } else {
      CompositeFuture composite = CompositeFuture.all(promises.stream().map(Promise::future).collect(Collectors.toList()));
      composite.onComplete(ar -> {
        discovery.close();
        if(ar.succeeded()) {
          promise.complete();
        } else {
          promise.fail(ar.cause());
        }
      });
    }
  }
}
