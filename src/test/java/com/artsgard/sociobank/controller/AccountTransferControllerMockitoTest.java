package com.artsgard.sociobank.controller;

import com.artsgard.sociobank.dto.AccountTransferDTO;
import com.artsgard.sociobank.service.MapperService;
import com.artsgard.sociobank.serviceimpl.AccountTransferServiceImpl;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AccountTransferControllerMockitoTest {
    
    private MockMvc mockMvc;

    @Mock
    private AccountTransferServiceImpl service;

    @InjectMocks
    AccountTransferController controller;
    
    private JacksonTester<AccountTransferDTO> jsonTransfer;
    private JacksonTester<List<AccountTransferDTO>> jsonTransfers;
    
    @Mock
    private MapperService mapperService;

    private AccountTransferDTO transfer1;
    private AccountTransferDTO transfer2;
    private List<AccountTransferDTO> transfers;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        Date now =  new Date();
        transfer1 = new AccountTransferDTO("iban-1", "iban-2", new BigDecimal("23.56"), now, "some first description");
        transfer2 = new AccountTransferDTO("iban-2", "iban-3", new BigDecimal("73.56"), now, "some second description");
       
        transfers = new ArrayList();
        transfers.add(transfer1);
        transfers.add(transfer2);
    }
    
    @Test
    void injectedComponentsAreNotNull() {
        assertThat(mockMvc).isNotNull();
        assertThat(jsonTransfer).isNotNull();
        assertThat(jsonTransfers).isNotNull();
    }

    @Test
    public void testFindAlltransfers() throws Exception {

        given(service.findAllAccountTransfers()).willReturn(transfers);

        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/transfer"))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonTransfers.write(transfers).getJson()
        );
    }

    @Test
    public void testFindTransferById() throws Exception {
        given(service.findAccountTransferById(any(Long.class))).willReturn(transfer1);

        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/transfer/".concat("1")))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonTransfer.write(transfer1).getJson()
        );
    }

    @Test
    public void  testFindTransfersByAccountSourceId() throws Exception {
        given(service.findAccountTransfersByAccountSourceId(any(Long.class))).willReturn(transfers);

        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/transfer/accountSourceId/".concat("1")))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonTransfers.write(transfers).getJson()
        );
    }
    
    @Test
    public void testFindTransfersByIban() throws Exception {
        given(service.findAccountTransfersByIban(any(String.class))).willReturn(transfers);

        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/transfer/iban/".concat("iban-1")))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonTransfers.write(transfers).getJson()
        );
    }
    
    @Test
    public void testFindTransfersByUsername() throws Exception {
        given(service.findAccountTransfersByUsername(any(String.class))).willReturn(transfers);

        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/transfer/username/".concat("wd")))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonTransfers.write(transfers).getJson()
        );
    }
    
    @Test
    public void testFindTransfersByIds() throws Exception {
        given(service.findAccountTransferByIds(any(Long.class), any(Long.class))).willReturn(transfer1);

        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/transfer".concat("/1").concat("/2")))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonTransfer.write(transfer1).getJson()
        );
    }

    @Test
    public void testSaveTransfer() throws Exception {
        given(service.saveAccountTransfer(any(AccountTransferDTO.class))).willReturn(transfer1);
        
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.post("/transfer/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)      
                .content(jsonTransfer.write(transfer1)
                .getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonTransfer.write(transfer1).getJson());
    }
}
