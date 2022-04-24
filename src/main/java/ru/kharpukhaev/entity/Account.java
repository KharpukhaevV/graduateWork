package ru.kharpukhaev.entity;

import ru.kharpukhaev.entity.enums.AccountType;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "account_id", unique = true, nullable = false)
    private long id;

    private String number;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    private long balance;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Account() {
    }

    public Account(AccountType type, Client client) {
        this.number = "446644" + (1000000000 + (long) (Math.random() * 9999999999L));
        this.type = type;
        this.client = client;
    }

    public long getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}