package ru.kharpukhaev.entity;

import ru.kharpukhaev.entity.enums.CreditStatus;
import ru.kharpukhaev.entity.enums.Currency;

import javax.persistence.*;

@Entity
@Table(name = "credits")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "card_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Enumerated(EnumType.STRING)
    private CreditStatus status;

    private long limitCard;

    private double percent;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String startDate;

    private String expirationDate;

    public Credit() {
    }

    public Credit(Client client, Currency currency) {
        this.client = client;
        this.currency = currency;
        this.status = CreditStatus.PROCESSED;
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

    public CreditStatus getStatus() {
        return status;
    }

    public void setStatus(CreditStatus status) {
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
