package com.lee.virtualno.user.databases;

import com.lee.virtualno.user.entity.VirtualNoUser;
import io.vertx.core.Future;
import io.vertx.pgclient.PgPool;

import java.util.HashMap;

public interface VirtualNoUserDataService {
  Future<VirtualNoUser> fetchUser(String username);

  Future<Void> updateUser(VirtualNoUser user);

  static VirtualNoUserDataService create(HashMap<String, String> sqlQueries, PgPool pgPool) {
    return new VirtualNoUserDataServiceImpl(sqlQueries, pgPool);
  }

}
