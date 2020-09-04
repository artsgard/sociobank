package com.artsgard.sociobank.controller;

import com.artsgard.sociobank.dto.AccountDTO;
import com.artsgard.sociobank.exception.ResourceNotFoundException;
import com.artsgard.sociobank.serviceimpl.AccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

/**
 *
 * @author artsgard
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureJsonTesters
public class AccountControllerServerTest {

    @MockBean
    private AccountServiceImpl accountService;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private JacksonTester<AccountDTO> jsonAccount;

    @InjectMocks
    AccountController  accountController;
    
    private AccountDTO account1;
    private AccountDTO account2;
    private AccountDTO account3;
    private List<AccountDTO> accounts;

    //private final MapperService mapperService = new MapperServiceImpl();

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        Date now =  new Date();
        account1 = new AccountDTO(null, "username1", "iban-x", new BigDecimal("23.56"), "EUR", now, true);
        account2 = new AccountDTO(null, "username2", "iban-xx", new BigDecimal("53.56"), "USD", now, true);
        account3 = new AccountDTO(null, "username3", "iban-xxx", new BigDecimal("73.56"), "EUR", now, true);
        accounts = new ArrayList();
        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
    }

    @Test
    public void testFindAllAccounts() throws Exception {
        given(accountService.findAllAccounts())
                .willReturn(accounts);

        ResponseEntity<AccountDTO[]> response = restTemplate
                .getForEntity("/account", AccountDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(accounts));

    }

    @Test
    public void testFindSocioById() throws Exception {
        given(accountService.findAccountById(1L)).willReturn(account1);

        ResponseEntity<AccountDTO> response = restTemplate.getForEntity("/account/".concat("1"), AccountDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(account1));
    }

    @Test
    public void testFindAccountByIban() throws Exception {
        given(accountService.findAccountByIban("iban-x")).willReturn(account1);

        ResponseEntity<AccountDTO> response = restTemplate.getForEntity("/account/iban/".concat("iban-x"), AccountDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(account1));
    }
    
    @Test
    public void testFindAccountByUsername() throws Exception {
        given(accountService.findAccountByUsername("wd")).willReturn(account1);

        ResponseEntity<AccountDTO> response = restTemplate.getForEntity("/account/username/".concat("wd"), AccountDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(account1));
    }

    @Test
    public void testSaveAccount() throws Exception {
        ResponseEntity<AccountDTO> response = restTemplate.postForEntity("/account", account1, AccountDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void testUpdatesocio() throws Exception {
        given(accountService.updateAccount(any(AccountDTO.class))).willReturn(account1);
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonAccount.write(account1).getJson(), headers);
        
        ResponseEntity<String> response = restTemplate.exchange("/account/", HttpMethod.PUT, entity, String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(account1));
    }
    
    @Test
    public void testDeleteSocio() throws Exception {
        try {
             restTemplate.delete("/account/".concat("1"), AccountDTO.class);
        } catch (HttpClientErrorException ex) {
            String message = ex.getResponseBodyAsString();
            throw new ResourceNotFoundException(message);
        }
    }
}
