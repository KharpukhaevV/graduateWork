package ru.kharpukhaev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "contributions_offer")
public class ContributionOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "contribution_offer_id", unique = true, nullable = false)
    private Long id;

    @NotEmpty
    private String name;

    @Positive
    @NotNull
    private Double stake;
    @NotNull
    private Boolean takeOff;
    @NotNull
    private Boolean topUp;
    @NotNull
    private Boolean isCurrency;
    @Positive
    @NotNull
    private Integer minTerm;

    private LocalDate term;
    @NotNull
    private Boolean canBeClosed;


    public ContributionOffer() {
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

    public Boolean getCanBeClosed() {
        return canBeClosed;
    }

    public void setCanBeClosed(Boolean canBeClosed) {
        this.canBeClosed = canBeClosed;
    }
}
