package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.entity.Role;
import org.kosiuk.webApp.exceptions.NotCompatibleRolesException;
import org.kosiuk.webApp.util.visitor.Visitor;
import org.kosiuk.webApp.util.visitor.VisitorAcceptor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserCreationDto implements UserRoleProvider, VisitorAcceptor {
    private String username;
    private String email;
    private String password;
    boolean user;
    boolean admin;

    public UserCreationDto(String username, String email, String password, boolean user, boolean admin) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.user = user;
        this.admin = admin;
    }

    public UserCreationDto() {

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    public Set<Role> getCheckedRoles() throws NotCompatibleRolesException{
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
        return visitor.visitUserCreationDto(this);
    }
}
