package ru.kharpukhaev.entity;

import ru.kharpukhaev.entity.enums.CreditStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "credits")
public class CreditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "credit_id", unique = true, nullable = false)
    private Long id;

    private Long sum;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;

    private Long penalties;

    public CreditEntity() {
    }

    public CreditEntity(Long sum, Card card) {
        this.sum = sum;
        this.card = card;
        this.penalties = 0L;
        this.creditStatus = CreditStatus.ACTIVE;
        LocalDate localDate = LocalDate.now().plusMonths(1);
        if (localDate.isBefore(card.getCreditBid().getExpirationDate())) {
            this.expirationDate = LocalDate.now().plusMonths(1);
        } else {
            this.expirationDate = card.getCreditBid().getExpirationDate().plusDays(1);
        }
    }

    public Long getId() {
        return id;
    }


    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public CreditStatus getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(CreditStatus creditStatus) {
        this.creditStatus = creditStatus;
    }

    public Long getPenalties() {
        return penalties;
    }

    public void setPenalties(Long penalties) {
        this.penalties = penalties;
    }
}
