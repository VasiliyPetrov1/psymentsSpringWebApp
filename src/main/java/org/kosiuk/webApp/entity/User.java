package org.kosiuk.webApp.entity;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "account", uniqueConstraints = {@UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "username")})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(length = 30)
    @NonNull
    @NotEmpty(message = "Field username is required to be filled.")
    @Size(min = 2, max = 30, message = "Name length should be between 2 and 30.")
    private String username;

    @Column(length = 30)
    @NotEmpty(message = "Field email is required to be filled.")
    @Email(message = "Enter valid email please.")
    private String email;

    @Column(length = 16)
    @NonNull
    @NotEmpty(message = "Field password is required to be filled.")
    @Size(min = 4, max = 16)
    private String password;

    @Column(name = "has_order_on_check")
    @NonNull
    private boolean hasOrderOnCheck;

    @Column(name = "active", nullable = false)
    @NonNull
    private boolean active;

    @Column(name = "has_blocked_account", nullable = false)
    @NonNull
    private boolean hasBlockedAccount;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "role", joinColumns = @JoinColumn(name = "account_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<CreditCard> creditCards;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<CreditCardOrder> creditCardOrders;

    public User(Integer id, String username, String email, boolean active, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.active = active;
        this.roles = roles;
    }

    public User(String username, String email, String password, boolean active, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.active = active;
        this.roles = roles;
    }

    public User(Integer id, @NonNull String username, String email, @NonNull String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String username;
        private String email;
        private String password;
        private boolean active;
        private boolean hasOrderOnCheck;
        private boolean hasBlockedAccount;
        private Set<Role> roles = new HashSet<>();;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder initRegistrationDetails(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
            return this;
        }

        public Builder initFlagsDefault() {
            this.hasBlockedAccount = false;
            this.hasOrderOnCheck = false;
            this.active = true;
            return this;
        }

        public Builder roles(Role ... roleArgs) {
            for (Role curRole : roleArgs) {
                roles.add(curRole);
            }
            return this;
        }

        public User build() {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setActive(active);
            user.setHasBlockedAccount(hasBlockedAccount);
            user.setHasOrderOnCheck(hasOrderOnCheck);
            user.setRoles(roles);
            return user;
        }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isHasBlockedAccount() {
        return hasBlockedAccount;
    }

    public void setHasBlockedAccount(boolean hasBlockedAccount) {
        this.hasBlockedAccount = hasBlockedAccount;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    public List<CreditCardOrder> getCreditCardOrders() {
        return creditCardOrders;
    }

    public void setCreditCardOrders(List<CreditCardOrder> creditCardOrders) {
        this.creditCardOrders = creditCardOrders;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
