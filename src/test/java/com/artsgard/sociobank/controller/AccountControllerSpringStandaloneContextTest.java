package com.artsgard.sociobank.controller;

import com.artsgard.sociobank.dto.AccountDTO;
import com.artsgard.sociobank.service.MapperService;
import com.artsgard.sociobank.serviceimpl.AccountServiceImpl;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(AccountController.class)
public class AccountControllerSpringStandaloneContextTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountServiceImpl accountService;

    @InjectMocks
    AccountController accountController;
    
    @Autowired
    private JacksonTester<AccountDTO> jsonAccount;
    
    @Autowired
    private JacksonTester<List<AccountDTO>> jsonAccounts;
    
    @Mock
    private MapperService mapperService;

    private AccountDTO account1;
    private AccountDTO account2;
    private AccountDTO account3;
    private List<AccountDTO> accounts;

    @BeforeEach
    public void setup() {
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
    void injectedComponentsAreNotNull() {
        assertThat(mockMvc).isNotNull();
        assertThat(jsonAccounts).isNotNull();
    }

    @Test
    public void testFindAllAccounts() throws Exception {

        given(accountService.findAllAccounts()).willReturn(accounts);

        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/account"))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonAccounts.write(accounts).getJson()
        );
    }

    @Test
    public void testFindAccountById() throws Exception {
        given(accountService.findAccountById(1L)).willReturn(account1);

        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/account/".concat("1")))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonAccount.write(account1).getJson()
        );
    }

    @Test
    public void testFindAccountByIban() throws Exception {
        given(accountService.findAccountByIban("iban-x")).willReturn(account1);

        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/account/iban/".concat("iban-x")))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonAccount.write(account1).getJson()
        );
    }
    
     @Test
    public void testFindAccountByUsername() throws Exception {
        given(accountService.findAccountByUsername("wd")).willReturn(account1);

        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/account/username/".concat("wd")))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonAccount.write(account1).getJson()
        );
    }

    @Test
    public void testSaveAccount() throws Exception {
        given(accountService.saveAccount(any(AccountDTO.class))).willReturn(account1);
        
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.post("/account/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)      
                .content(jsonAccount.write(account1)
                .getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonAccount.write(account1).getJson());
    }

    @Test
    public void testUpdateAccount() throws Exception {
        given(accountService.updateAccount(any(AccountDTO.class))).willReturn(account1);
        
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.put("/account/")
                .contentType(MediaType.APPLICATION_JSON)
            .content(jsonAccount.write(account1)
                .getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonAccount.write(account1).getJson());
    }

    @Test
    public void testDeleteSocio() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/account/{id}", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}