package ru.kharpukhaev.entity;

import ru.kharpukhaev.entity.enums.CreditBidStatus;
import ru.kharpukhaev.entity.enums.Currency;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "credit_bids")
public class CreditBid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bid_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Enumerated(EnumType.STRING)
    private CreditBidStatus status;

    private long limitCard;

    private double percent;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private LocalDate startDate;

    private LocalDate expirationDate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "card_id")
    private Card card;

    public CreditBid() {
    }

    public CreditBid(Client client, Currency currency) {
        this.client = client;
        this.currency = currency;
        this.status = CreditBidStatus.PROCESSED;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public CreditBidStatus getStatus() {
        return status;
    }

    public void setStatus(CreditBidStatus status) {
        this.status = status;
    }

    public long getLimitCard() {
        return limitCard;
    }

    public void setLimitCard(long limit) {
        this.limitCard = limit;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
