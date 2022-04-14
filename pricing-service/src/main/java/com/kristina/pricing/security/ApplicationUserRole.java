package com.kristina.pricing.security;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.kristina.pricing.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
  CLIENT(Sets.newHashSet()),
  ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE));

  private final Set<ApplicationUserPermission> permissions;

  ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
    this.permissions = permissions;
  }

  public Set<ApplicationUserPermission> getPermissions() {
    return permissions;
  }
}

