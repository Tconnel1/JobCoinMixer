package com.example.mixer.service;

import com.example.mixer.Repository.MixerRepository;
import com.example.mixer.model.AddressInfo;
import com.example.mixer.model.MixerRecord;
import com.example.mixer.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddressService {
    public static final String URI = "http://jobcoin.gemini.com/violate-shucking/api/" ;
    private static final String HOUSE_ADDRESS = "House Address";

    private final MixerRepository mixerRepository;
    private final RestTemplate restTemplate;

    /**
     * creates a deposit address as a random string
     * @param toAddresses a set of addresses that the deposit address will be linked to
     * @return a random deposit address that is linked to the users addresses
     */
    public String createDepositAddress(Set<String> toAddresses) {
        log.info("Creating new deposit address");
        String depositAddress = UUID.randomUUID().toString();
        MixerRecord mixerRecord = new MixerRecord(depositAddress, toAddresses);
        mixerRepository.add(depositAddress, mixerRecord);
        return depositAddress;
    }

    /**
     * checks if the users inputted address are valid
     * @param addresses list of addresses that the user has input
     * @return a set of addresses that are invalid, this can be empty
     */
    public Set<String> getInvalidAddresses(Set<String> addresses) {
        Set<String> invalidAddresses = new HashSet<>();
        log.info("Checking user addresses for invalid addresses");
        addresses.forEach(address ->  {
            AddressInfo addressInfo = restTemplate.getForObject(URI + "addresses/" + address, AddressInfo.class);
            if(addressInfo != null) {
                invalidAddresses.addAll(addressInfo.getTransactions()
                        .stream()
                        .map(Transaction::getToAddress)
                        .collect(Collectors.toList()));
            }
        });
        return invalidAddresses;
    }
}
