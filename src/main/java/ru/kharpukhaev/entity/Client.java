package ru.kharpukhaev.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kharpukhaev.entity.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "client_id", unique = true, nullable = false)
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 16)
    private String username;

    @NotEmpty
    @Size(min = 4, max = 16)
    private String password;

    @NotEmpty
    @Size(min = 2, max = 16)
    private String firstname;

    @NotEmpty
    @Size(min = 2, max = 16)
    private String lastname;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "client", orphanRemoval = true)
    private Set<Card> cards;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "client", orphanRemoval = true)
    private Set<Account> accounts;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "sender", orphanRemoval = true)
    private Set<TransferEntity> expenses;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "recipient", orphanRemoval = true)
    private Set<TransferEntity> receiving;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "client", orphanRemoval = true)
    private Set<CreditBid> creditBids;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "client_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> card) {
        this.cards = card;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<TransferEntity> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<TransferEntity> transfers) {
        this.expenses = transfers;
    }

    public Set<TransferEntity> getReceiving() {
        return receiving;
    }

    public void setReceiving(Set<TransferEntity> receiving) {
        this.receiving = receiving;
    }

    public Set<CreditBid> getCredits() {
        return creditBids;
    }

    public void setCredits(Set<CreditBid> creditBids) {
        this.creditBids = creditBids;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return lastname + " " + firstname;
    }
}