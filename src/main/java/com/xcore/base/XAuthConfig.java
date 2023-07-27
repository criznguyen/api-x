package com.xcore.base;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;

public class XAuthConfig extends XUnit {
  private static JWTAuth jwtAuth;
  public XAuthConfig(Vertx vertx, JsonObject config) {
    super(vertx, config);
    KeyStoreOptions options = new KeyStoreOptions(new JsonObject()
      .put("type", "HS256")
      .put("key", config.getString("JWT_SECRET")));
    jwtAuth = JWTAuth.create(vertx, new JWTAuthOptions().setKeyStore(options));
  }

  public String generateToken(JWTAuth jwtAuth, String subject, String role) {
    // Create JWT claims
    JsonObject claims = new JsonObject()
      .put("sub", subject)
      .put("role", role);

    // Generate JWT token with expiration time (optional)
    JWTOptions options = new JWTOptions().setExpiresInSeconds(getConfig().getInteger("JWT_EXPIRES")); // Token expires in 1 hour

    // Sign the claims and generate the JWT token
    return jwtAuth.generateToken(claims, options);
  }
}
