package com.example.mixer.controller;

import com.example.mixer.responses.MixerResponse;
import com.example.mixer.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("address")
@Slf4j
public class AddressController {
    private final AddressService addressService;
    @GetMapping("/invalid-addresses")
    public MixerResponse getInvalidAddresses(@QueryParam("addresses") Set<String> addresses) {
        Set<String> invalidAddresses = addressService.getInvalidAddresses(addresses);
        if (invalidAddresses.equals(Collections.emptySet())) {
            log.info("All user addresses are valid");
            return new MixerResponse("OK", Optional.of(addressService.createDepositAddress(addresses)), Optional.empty());
        } else {
            log.error("Invalid user addresses have been found");
            return new MixerResponse("ERROR", Optional.empty(), Optional.of(invalidAddresses));
        }
    }
}
