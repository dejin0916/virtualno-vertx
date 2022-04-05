package com.lee.virtualno.user.databases;

import com.lee.virtualno.user.entity.VirtualNoUser;
import com.lee.virtualno.user.entity.VirtualNoUserParametersMapper;
import com.lee.virtualno.user.entity.VirtualNoUserRowMapper;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;

public class VirtualNoUserDataServiceImpl implements VirtualNoUserDataService {
  private static final Logger logger = LoggerFactory.getLogger(VirtualNoUserDataServiceImpl.class);
  private final HashMap<String, String> sqlQueries;
  private final PgPool pgPool;

  public VirtualNoUserDataServiceImpl(HashMap<String, String> sqlQueries, PgPool pgPool) {
    this.sqlQueries = sqlQueries;
    this.pgPool = pgPool;
    pgPool.getConnection()
      .onSuccess(success -> logger.info("user service connect database success"))
      .onFailure(err -> logger.error("user service connect database failed", err));
  }

  @Override
  public Future<VirtualNoUser> fetchUser(String username) {
    Promise<VirtualNoUser> result = Promise.promise();
    SqlTemplate.forQuery(pgPool, sqlQueries.get("fetch-user"))
      .mapTo(VirtualNoUserRowMapper.INSTANCE)
      .execute(Collections.singletonMap("username", username))
      .onSuccess(users -> {
        result.complete(users.iterator().next());
      })
      .onFailure(err -> {
        logger.error("fetch all apps caught error", err);
        result.fail(err);
      });
    return result.future();
  }

  @Override
  public Future<Void> updateUser(VirtualNoUser user) {
    Promise<Void> result = Promise.promise();
    SqlTemplate.forUpdate(pgPool, sqlQueries.get("update-user"))
      .mapFrom(VirtualNoUserParametersMapper.INSTANCE)
      .execute(user)
      .onSuccess(success -> {
        logger.info("update user success");
        result.complete();
      })
      .onFailure(err -> {
        logger.error("update user failed", err);
        result.fail(err);
      });
    return result.future();
  }
}
