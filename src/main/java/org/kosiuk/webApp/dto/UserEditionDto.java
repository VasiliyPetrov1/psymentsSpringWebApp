package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.entity.Role;
import org.kosiuk.webApp.exceptions.NotCompatibleRolesException;
import org.kosiuk.webApp.util.visitor.Visitor;
import org.kosiuk.webApp.util.visitor.VisitorAcceptor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserEditionDto implements UserRoleProvider, VisitorAcceptor {
    private Integer id;
    private String username;
    private String email;
    boolean active;
    boolean user;
    boolean admin;

    public UserEditionDto(Integer id, String username, String email, boolean active, boolean user, boolean admin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.active = active;
        this.user = user;
        this.admin = admin;
    }

    public UserEditionDto() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isUser() {
        return user;
    }

    public void setUser(boolean user) {
        this.user = user;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public Set<Role> getCheckedRoles() throws NotCompatibleRolesException {
        Set<Role> roleSet = new HashSet<>();

        if (isUser() && isAdmin()) {
            roleSet.add(Role.USER);
            roleSet.add(Role.ADMIN);
            StringBuilder sb = new StringBuilder();
            for (Role role: roleSet) {
                sb.append(role.name());
                sb.append(" ");
            }
            throw new NotCompatibleRolesException(sb.toString());
        } else if (isUser()) {
            roleSet.add(Role.USER);
        } else if (isAdmin()) {
            roleSet.add(Role.ADMIN);
        }
        return roleSet;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitUserEditionDto(this);
    }
}
