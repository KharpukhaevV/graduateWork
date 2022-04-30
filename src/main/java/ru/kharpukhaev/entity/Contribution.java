package ru.kharpukhaev.entity;

import ru.kharpukhaev.entity.enums.Currency;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDate;

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

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private LocalDate term;

    private Double stake;

    private Boolean takeOf;

    private Boolean takeUp;

    @Min(100)
    private long sum;

    public Contribution() {
    }

    public Contribution(Client client, Currency currency, LocalDate term, Double stake, Boolean takeOf, Boolean takeUp) {
        this.client = client;
        this.currency = currency;
        this.term = term;
        this.stake = stake;
        this.takeOf = takeOf;
        this.takeUp = takeUp;
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

    public LocalDate getTerm() {
        return term;
    }

    public void setTerm(LocalDate term) {
        this.term = term;
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

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }
}
