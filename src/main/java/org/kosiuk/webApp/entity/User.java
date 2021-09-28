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
    private String username;

    @Column(length = 30)
    private String email;

    @Column(length = 16)
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

    public User(Integer id, @NonNull String username, String email, @Size(min = 4, max = 16) String password,
                boolean hasOrderOnCheck, boolean active, boolean hasBlockedAccount, Set<Role> roles,
                List<CreditCard> creditCards, List<CreditCardOrder> creditCardOrders) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.hasOrderOnCheck = hasOrderOnCheck;
        this.active = active;
        this.hasBlockedAccount = hasBlockedAccount;
        this.roles = roles;
        this.creditCards = creditCards;
        this.creditCardOrders = creditCardOrders;
    }

    public User() {

    }
    public static User.Builder builder() {
        return new User.Builder();
    }

    public static class Builder {

        private Integer id;
        private String username;
        private String email;
        private String password;
        private boolean active;
        private boolean hasOrderOnCheck;
        private boolean hasBlockedAccount;
        private Set<Role> roles = new HashSet<>();
        private List<CreditCard> creditCards;
        private List<CreditCardOrder> creditCardOrders;

        public User.Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public User.Builder username(String username) {
            this.username = username;
            return this;
        }

        public User.Builder password(String password) {
            this.password = password;
            return this;
        }

        public User.Builder email(String email) {
            this.email = email;
            return this;
        }

        public User.Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public User.Builder hasBlockedAccount(boolean hasBlockedAccount) {
            this.hasBlockedAccount = hasBlockedAccount;
            return this;
        }

        public User.Builder hasOrderOnCheck(boolean hasOrderOnCheck) {
            this.hasOrderOnCheck = hasOrderOnCheck;
            return this;
        }

        public User.Builder roleSet(Set<Role> roleSet) {
            this.roles = roleSet;
            return this;
        }

        public User.Builder creditCards(List<CreditCard> creditCards) {
            this.creditCards = creditCards;
            return this;
        }

        public User.Builder creditCardOrders(List<CreditCardOrder> orders) {
            this.creditCardOrders = orders;
            return this;
        }

        public User.Builder initRegistrationDetails(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
            return this;
        }

        public User.Builder initFlagsDefault() {
            this.hasBlockedAccount = false;
            this.hasOrderOnCheck = false;
            this.active = true;
            return this;
        }

        public User.Builder roles(Role ... roleArgs) {
            for (Role curRole : roleArgs) {
                roles.add(curRole);
            }
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setActive(active);
            user.setHasBlockedAccount(hasBlockedAccount);
            user.setHasOrderOnCheck(hasOrderOnCheck);
            user.setRoles(roles);
            user.setCreditCards(creditCards);
            user.setCreditCardOrders(creditCardOrders);
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
