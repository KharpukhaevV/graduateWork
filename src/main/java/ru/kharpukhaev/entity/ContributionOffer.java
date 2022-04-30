package ru.kharpukhaev.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contributions_offer")
public class ContributionOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "contribution_offer_id", unique = true, nullable = false)
    private Long id;

    private String name;

    private Double stake;

    private Boolean takeOff;

    private Boolean topUp;

    private Boolean isCurrency;

    private Integer minTerm;

    private LocalDate term;


    public ContributionOffer() {
    }

    public ContributionOffer(String name, Double stake, Integer minTerm, Boolean takeOff, Boolean topUp, Boolean isCurrency) {
        this.name = name;
        this.stake = stake;
        this.minTerm = minTerm;
        this.takeOff = takeOff;
        this.topUp = topUp;
        this.isCurrency = isCurrency;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getStake() {
        return stake;
    }

    public void setStake(Double stake) {
        this.stake = stake;
    }

    public Boolean getTakeOff() {
        return takeOff;
    }

    public void setTakeOff(Boolean takeOff) {
        this.takeOff = takeOff;
    }

    public Boolean getTopUp() {
        return topUp;
    }

    public void setTopUp(Boolean topUp) {
        this.topUp = topUp;
    }

    public Integer getMinTerm() {
        return minTerm;
    }

    public void setMinTerm(Integer minTerm) {
        this.minTerm = minTerm;
    }

    public LocalDate getTerm() {
        return term;
    }

    public void setTerm(LocalDate term) {
        this.term = term;
    }

    public Boolean getCurrency() {
        return isCurrency;
    }

    public void setCurrency(Boolean currency) {
        isCurrency = currency;
    }
}
