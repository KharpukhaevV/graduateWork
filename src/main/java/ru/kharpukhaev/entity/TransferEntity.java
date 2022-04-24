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
    @JoinColumn(name = "sender_id", nullable = false)
    private Account sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Account recipient;

    private long transferSum;

    private String date;
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    public TransferEntity() {
    }

    public TransferEntity(Account sender, Account recipient, long transferSum) {
        this.transferSum = transferSum;
        this.status = TransferStatus.PROCESSED;
        this.sender = sender;
        this.recipient = recipient;
        this.date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

    }

    public Long getId() {
        return id;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getRecipient() {
        return recipient;
    }

    public void setRecipient(Account recipient) {
        this.recipient = recipient;
    }

    public long getTransferSum() {
        return transferSum;
    }

    public void setTransferSum(long transferSum) {
        this.transferSum = transferSum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Sender=" + sender +
                ", AccountNumber=" + recipient +
                ", TransferSum=" + transferSum;
    }
}