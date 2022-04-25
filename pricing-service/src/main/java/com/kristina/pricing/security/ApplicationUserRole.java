package com.kristina.pricing.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.kristina.pricing.security.ApplicationUserPermission.PRICE_READ;
import static com.kristina.pricing.security.ApplicationUserPermission.PRICE_WRITE;

public enum ApplicationUserRole {
  CLIENT(Sets.newHashSet()),
  ADMIN(Sets.newHashSet(PRICE_READ, PRICE_WRITE)),
  ADMINTRAINEE(Sets.newHashSet(PRICE_READ));

  private final Set<ApplicationUserPermission> permissions;

  ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
    this.permissions = permissions;
  }

  public Set<ApplicationUserPermission> getPermissions() {
    return permissions;
  }

  public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
    Set<SimpleGrantedAuthority> permissions =
        getPermissions().stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toSet());
    permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return permissions;
  }
}
