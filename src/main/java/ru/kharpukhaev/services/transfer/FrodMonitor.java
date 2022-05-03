package ru.kharpukhaev.services.transfer;

import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.TransferStatus;
import ru.kharpukhaev.repository.TransferRepository;

@Component
public class FrodMonitor {

    private final TransferRepository transferRepository;

    public FrodMonitor(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public boolean monitor(TransferEntity transfer){
        if ((transfer.getTransferSum() % 2) == 0) {
            transfer.setStatus(TransferStatus.SUCCESS);
            transferRepository.save(transfer);
            return true;
        } else {
            transferRepository.save(transfer);
            return false;
        }
    }
}
