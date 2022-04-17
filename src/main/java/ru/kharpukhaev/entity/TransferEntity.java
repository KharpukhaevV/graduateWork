package ru.kharpukhaev.entity;

import ru.kharpukhaev.entity.enums.TransferStatus;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

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
    private Client sender;
    @Column(name = "account_number")
    @NotEmpty
    private String accountNumber;
    @Column(name = "transfer_sum")
    @Min(0)
    private long transferSum;
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    public TransferEntity() {
    }
    public TransferEntity(Client sender, String accountNumber, long transferSum) {
        this.sender = sender;
        this.accountNumber = accountNumber;
        this.transferSum = transferSum;
        this.status = TransferStatus.PROCESSED;
    }
    public Client getSender() {
        return sender;
    }
    public void setSender(Client sender) {
        this.sender = sender;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setRecipient(String recipient) {
        this.accountNumber = recipient;
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
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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
                ", AccountNumber=" + accountNumber +
                ", TransferSum=" + transferSum;
    }
}