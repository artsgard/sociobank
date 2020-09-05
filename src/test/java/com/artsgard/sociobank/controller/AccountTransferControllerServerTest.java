package com.artsgard.sociobank.controller;

import com.artsgard.sociobank.dto.AccountTransferDTO;
import com.artsgard.sociobank.service.MapperService;
import com.artsgard.sociobank.serviceimpl.AccountTransferServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author artsgard
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureJsonTesters
public class AccountTransferControllerServerTest {

    @MockBean
    private AccountTransferServiceImpl service;

    @Autowired
    private TestRestTemplate restTemplate;

    @InjectMocks
    AccountTransferController  controller;
    
    @Autowired
    private JacksonTester<AccountTransferDTO> jsonTransfer;
    
    @Autowired
    private JacksonTester<List<AccountTransferDTO>> jsonTransfers;
    
    @Mock
    private MapperService mapperService;

    private AccountTransferDTO transfer1;
    private AccountTransferDTO transfer2;
    private List<AccountTransferDTO> transfers;

    @BeforeEach
    public void setup() {
        Date now =  new Date();
        transfer1 = new AccountTransferDTO("iban-1", "iban-2", new BigDecimal("23.56"), "some first description", now);
        transfer2 = new AccountTransferDTO("iban-2", "iban-3", new BigDecimal("73.56"), "some second description", now);
       
        transfers = new ArrayList();
        transfers.add(transfer1);
        transfers.add(transfer2);
    }

    @Test
    public void testFindAllTransfers() throws Exception {
        given(service.findAllAccountTransfers())
                .willReturn(transfers);

        ResponseEntity<AccountTransferDTO[]> response = restTemplate
                .getForEntity("/transfer", AccountTransferDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(transfers));

    }

    @Test
    public void testFindTransferById() throws Exception {
        given(service.findAccountTransferById(any(Long.class))).willReturn(transfer1);

        ResponseEntity<AccountTransferDTO> response = restTemplate.getForEntity("/transfer/".concat("1"), AccountTransferDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(transfer1));
    }
    
    @Test
    public void testFindTransfersByAccountSourceId() throws Exception {
        given(service.findAccountTransfersByAccountSourceId(any(Long.class))).willReturn(transfers);

        ResponseEntity<AccountTransferDTO[]> response = restTemplate.getForEntity("/transfer/accountSourceId/".concat("1"), AccountTransferDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(transfers));
    }

    @Test
    public void testFindTransferByIban() throws Exception {
        given(service.findAccountTransfersByIban(any(String.class))).willReturn(transfers);

        ResponseEntity<AccountTransferDTO[]> response = restTemplate.getForEntity("/transfer/iban/".concat("iban-x"), AccountTransferDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(transfers));
    }
    
    @Test
    public void testFindTransferByUsername() throws Exception {
        given(service.findAccountTransfersByUsername(any(String.class))).willReturn(transfers);

        ResponseEntity<AccountTransferDTO[]> response = restTemplate.getForEntity("/transfer/username/".concat("wd"), AccountTransferDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(transfers));
    }
    
    @Test
    public void findTransfersByIds() throws Exception {
        given(service.findAccountTransferByIds(any(Long.class), any(Long.class))).willReturn(transfer1);

        ResponseEntity<AccountTransferDTO> response = restTemplate.getForEntity("/transfer".concat("/1").concat("/2"), AccountTransferDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(transfer1));
    }

    @Test
    public void testSaveTransfert() throws Exception {
        ResponseEntity<AccountTransferDTO> response = restTemplate.postForEntity("/transfer", transfer1, AccountTransferDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
