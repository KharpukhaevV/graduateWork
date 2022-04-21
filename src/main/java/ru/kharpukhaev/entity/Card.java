package ru.kharpukhaev.entity;

import ru.kharpukhaev.entity.enums.CardType;

import javax.persistence.*;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "card_id", unique = true, nullable = false)
    private Long id;


    private String number;

    @Enumerated(EnumType.STRING)
    private CardType type;

    private long balance;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }
}
