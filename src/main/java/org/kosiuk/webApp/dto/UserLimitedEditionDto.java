package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.util.visitor.Visitor;
import org.kosiuk.webApp.util.visitor.VisitorAcceptor;
import org.springframework.stereotype.Component;

@Component
public class UserLimitedEditionDto implements VisitorAcceptor {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private boolean hasOrderOnCheck;

    public UserLimitedEditionDto(Integer id, String username, String email, String password, boolean hasOrderOnCheck) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.hasOrderOnCheck = hasOrderOnCheck;
    }

    public UserLimitedEditionDto() {

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasOrderOnCheck() {
        return hasOrderOnCheck;
    }

    public void setHasOrderOnCheck(boolean hasOrderOnCheck) {
        this.hasOrderOnCheck = hasOrderOnCheck;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitUserLimEditionDto(this);
    }
}
