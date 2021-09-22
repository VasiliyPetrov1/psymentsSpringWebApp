package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.util.visitor.Visitor;
import org.kosiuk.webApp.util.visitor.VisitorAcceptor;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationDto implements VisitorAcceptor {
    private String username;
    private String email;
    private String password;

    public UserRegistrationDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserRegistrationDto() {

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

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitUserRegDto(this);
    }
}
