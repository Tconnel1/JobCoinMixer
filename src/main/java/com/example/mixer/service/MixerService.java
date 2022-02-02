package com.example.mixer.service;

import com.example.mixer.Repository.MixerRepository;
import com.example.mixer.enums.Status;
import com.example.mixer.model.AddressInfo;
import com.example.mixer.model.MixerRecord;
import com.example.mixer.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MixerService {
    public static final String URI = "http://jobcoin.gemini.com/violate-shucking/api/" ;
    private static final String HOUSE_ADDRESS = "House Address";

    private final MixerRepository mixerRepository;
    private final RestTemplate restTemplate;

    /**
     * looks for records awaiting to be deposited and progresses them to the beginning of the transfer process
     */
    public void checkForDeposits() {
        log.info("Checking for records awaiting deposit");
        List<MixerRecord> mixerRecords = mixerRepository.getRecordsAwaitingDeposit();
        mixerRecords.forEach(record -> {
            AddressInfo addressInfo = restTemplate.getForObject(URI + "addresses/" + record.getDepositAddress(), AddressInfo.class);
            if(addressInfo != null) {
                log.info("Preparing records for transfer");
                addressInfo.getTransactions().forEach(transaction -> {
                    if (Double.parseDouble(transaction.getAmount()) > 0.00) {
                        Transaction deposit = new Transaction(transaction.getToAddress(), HOUSE_ADDRESS, transaction.getAmount());
                        restTemplate.postForObject(URI + "transactions", deposit, Transaction.class);
                        MixerRecord newRecord = mixerRepository.get(record.getDepositAddress());
                        newRecord.setBalance(Double.parseDouble(transaction.getAmount()));
                        newRecord.setStatus(Status.TRANSFERRING);
                        mixerRepository.update(record.getDepositAddress(), newRecord);
                    }
                });
            }
        });

    }

    /**
     * completes the transactions for the records that are ready to be transferred and updates their status to complete
     * @param mixerRecord the record containing where the transaction comes from, where it's going, and the amount of coins being transferred
     */
    public void transfer(MixerRecord mixerRecord) {
        log.info("Beginning transfer of prepared records");
        double percentChange = 100.00 / mixerRepository.getRecordsInProgress().size();
        double amount = (percentChange * mixerRecord.getBalance()) / mixerRecord.getToAddressList().size();
        mixerRecord.getToAddressList().forEach(address -> {
            Transaction transaction = new Transaction(HOUSE_ADDRESS, address, String.valueOf(amount));
            restTemplate.postForObject(URI + "transactions", transaction, Transaction.class);
            double completion = mixerRecord.getCompletion() + percentChange;
            Status status = (completion >= 99.99) ? Status.COMPLETE : Status.TRANSFERRING;
            mixerRecord.setCompletion(completion);
            mixerRecord.setStatus(status);
            mixerRepository.update(mixerRecord.getDepositAddress(), mixerRecord);
            log.info("Record has been successfully transferred, process is {}% complete", completion);
        });
    }
}
