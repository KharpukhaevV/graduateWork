package ru.kharpukhaev.entity;

import ru.kharpukhaev.entity.enums.TransferStatus;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "transfers")
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "transfer_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_card_id", nullable = false)
    private Card cardSender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_card_id", nullable = false)
    private Card cardRecipient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private Client sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Client recipient;

    private long transferSum;

    private String date;
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    public TransferEntity() {
    }

    public TransferEntity(Card cardSender, Card cardRecipient, long transferSum) {
        this.cardSender = cardSender;
        this.cardRecipient = cardRecipient;
        this.transferSum = transferSum;
        this.status = TransferStatus.PROCESSED;
        this.sender = cardSender.getClient();
        this.recipient = cardRecipient.getClient();
        this.date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

    }

    public Card getCardSender() {
        return cardSender;
    }

    public void setCardSender(Card sender) {
        this.cardSender = sender;
    }

    public Card getCardRecipient() {
        return cardRecipient;
    }

    public void setCardRecipient(Card recipient) {
        this.cardRecipient = recipient;
    }

    public long getTransferSum() {
        return transferSum;
    }

    public void setTransferSum(long transferSum) {
        this.transferSum = transferSum;
    }

    public Long getId() {
        return id;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public Client getSender() {
        return sender;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public Client getRecipient() {
        return recipient;
    }

    public void setRecipient(Client recipient) {
        this.recipient = recipient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Sender=" + cardSender +
                ", AccountNumber=" + cardRecipient +
                ", TransferSum=" + transferSum;
    }
}