package ru.kharpukhaev.entity;

import ru.kharpukhaev.entity.enums.AccountType;
import ru.kharpukhaev.entity.enums.CardType;
import ru.kharpukhaev.entity.enums.Currency;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "card_id", unique = true, nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private CardType type;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "card", orphanRemoval = true)
    private CreditBid creditBid;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "card", orphanRemoval = true)
    private Set<CreditEntity> credits;

    private LocalDate expirationDate;

    public Card() {
    }

    public Card(CardType type, Client client) {
        this.account = new Account(AccountType.CHECKING_ACCOUNT, client, Currency.RUB);
        this.type = type;
        this.client = client;
        this.expirationDate = LocalDate.now().plusYears(1);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CreditBid getCreditBid() {
        return creditBid;
    }

    public void setCreditBid(CreditBid creditBid) {
        this.creditBid = creditBid;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Set<CreditEntity> getCredits() {
        return credits;
    }

    public void setCredits(Set<CreditEntity> credits) {
        this.credits = credits;
    }

    public Long getId() {
        return id;
    }
}
