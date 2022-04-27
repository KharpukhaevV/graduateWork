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

    private String accountSender;

    private String accountRecipient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Client recipient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private Client sender;

    private Long transferSum;

    private String date;
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    public TransferEntity() {
    }

    public TransferEntity(Account accountSender, Account accountRecipient, long transferSum) {
        this.sender = accountSender.getClient();
        this.recipient = accountRecipient.getClient();
        this.transferSum = transferSum;
        this.status = TransferStatus.PROCESSED;
        this.accountSender = accountSender.getNumber();
        this.accountRecipient = accountRecipient.getNumber();
        this.date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

    }

    public Long getId() {
        return id;
    }

    public String getAccountSender() {
        return accountSender;
    }

    public void setAccountSender(String sender) {
        this.accountSender = sender;
    }

    public String getAccountRecipient() {
        return accountRecipient;
    }

    public void setAccountRecipient(String recipient) {
        this.accountRecipient = recipient;
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

    public Client getRecipient() {
        return recipient;
    }

    public void setRecipient(Client recipient) {
        this.recipient = recipient;
    }

    public Client getSender() {
        return sender;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public void setTransferSum(Long transferSum) {
        this.transferSum = transferSum;
    }

    @Override
    public String toString() {
        return "Sender=" + accountSender +
                ", AccountNumber=" + accountRecipient +
                ", TransferSum=" + transferSum;
    }
}