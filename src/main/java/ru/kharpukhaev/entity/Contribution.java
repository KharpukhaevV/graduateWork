package ru.kharpukhaev.entity;

import ru.kharpukhaev.entity.enums.Currency;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "contributions")
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "contribution_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @NotNull(message = "Выберете валюту")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String startDate;

    @NotEmpty(message = "Выберете дату")
    private String expirationDate;

    private Double stake;

    private Boolean takeOf;

    private Boolean takeUp;

    private Boolean canBeClosed;

    @Min(0)
    @NotNull(message = "Укажите сумму")
    private Long sum;

    public Contribution() {
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String term) {
        this.expirationDate = term;
    }

    public Double getStake() {
        return stake;
    }

    public void setStake(Double stake) {
        this.stake = stake;
    }

    public Boolean getTakeOf() {
        return takeOf;
    }

    public void setTakeOf(Boolean takeOf) {
        this.takeOf = takeOf;
    }

    public Boolean getTakeUp() {
        return takeUp;
    }

    public void setTakeUp(Boolean takeUp) {
        this.takeUp = takeUp;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Boolean getCanBeClosed() {
        return canBeClosed;
    }

    public void setCanBeClosed(Boolean canBeClosed) {
        this.canBeClosed = canBeClosed;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }
}
