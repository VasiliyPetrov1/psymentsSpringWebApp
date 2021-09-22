package org.kosiuk.webApp.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    USER, ADMIN;

    @Override
    public java.lang.String getAuthority() {
        return name();
    }
}
