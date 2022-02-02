package com.example.mixer.service;

import com.example.mixer.Repository.MixerRepository;
import com.example.mixer.model.AddressInfo;
import com.example.mixer.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddressServiceTest {

    @Mock
    private MixerRepository mixerRepository;

    @Mock
    RestTemplate restTemplate;

    private AddressService addressService;
    private AddressInfo addressInfo;
    private List<Transaction> transactions;

    @Before
    public void setUp() {
        addressService = new AddressService(mixerRepository, restTemplate);
        MockitoAnnotations.initMocks(this);
        Transaction transaction1 = new Transaction("NOW", "Bob's Account", "Stan's Account", "100");
        transactions = new ArrayList<>();
        transactions.add(transaction1);
        addressInfo = new AddressInfo(0L, transactions);
    }

    @Test
    public void testCreateDepositAddress() throws Exception {
        UUID address = UUID.fromString("da5a204a-9224-4831-bc17-0341690d4049");

        MockedStatic<UUID> mockedUuid = Mockito.mockStatic(UUID.class);
        mockedUuid.when(UUID::randomUUID).thenReturn(address);

        String result = addressService.createDepositAddress(new HashSet<>(List.of("String")));
        Assert.assertEquals("da5a204a-9224-4831-bc17-0341690d4049", result);
    }

    @Test
    public void testGetInvalidAddresses_invalidAddressesFound() throws Exception {
        Transaction transaction2 = new Transaction("NOW", "address1", "Stan's Account", "100");
        transactions.add(transaction2);
        addressInfo.setTransactions(transactions);

        when(restTemplate.getForObject("http://jobcoin.gemini.com/violate-shucking/api/addresses/address1", AddressInfo.class)).thenReturn(addressInfo);
        Set<String> result = addressService.getInvalidAddresses(new HashSet<>(List.of("address1")));

        Assert.assertEquals(new HashSet<>(Collections.emptySet()), result);
    }
}
