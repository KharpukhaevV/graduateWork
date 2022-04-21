package ru.kharpukhaev.entity;

import ru.kharpukhaev.entity.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "client_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "username")
    @NotEmpty
    @Size(min = 2, max = 16)
    private String username;

    @Column(name = "password")
    @NotEmpty
    @Size(min = 4, max = 16)
    private String password;

    @Column(name = "firstname")
    @NotEmpty
    @Size(min = 2, max = 16)
    private String firstname;

    @Column(name = "lastname")
    @NotEmpty
    @Size(min = 2, max = 16)
    private String lastname;

    @Column(name = "cards")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client")
    private Set<Card> cards;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "balance")
    private long balance;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn (name = "client_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;


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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
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


    @Override
    public String toString() {
        return lastname + " " + firstname;
    }
}