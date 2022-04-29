package ru.kharpukhaev.entity;

import ru.kharpukhaev.entity.enums.TransferStatus;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
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

    @NotEmpty
    private String accountSender;

    @NotEmpty
    private String accountRecipient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Client recipient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private Client sender;

    @Min(0)
    private long transferSum;

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

    public void setAccountSender(String accountSender) {
        this.accountSender = accountSender;
    }

    public String getAccountRecipient() {
        return accountRecipient;
    }

    public void setAccountRecipient(String accountRecipient) {
        this.accountRecipient = accountRecipient;
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

    public Long getTransferSum() {
        return transferSum;
    }

    public void setTransferSum(Long transferSum) {
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
        return "Sender=" + accountSender +
                ", AccountNumber=" + accountRecipient +
                ", TransferSum=" + transferSum;
    }
}