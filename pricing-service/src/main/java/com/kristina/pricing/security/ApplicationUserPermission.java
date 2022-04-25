package com.kristina.pricing.security;

public enum ApplicationUserPermission {
  PRICE_READ("price:read"),
  PRICE_WRITE("price:write");
  private final String permission;

  ApplicationUserPermission(String permission) {
    this.permission = permission;
  }

  public String getPermission() {
    return permission;
  }
}
