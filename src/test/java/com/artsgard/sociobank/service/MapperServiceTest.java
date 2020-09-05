package com.artsgard.sociobank.service;

import com.artsgard.sociobank.dto.AccountDTO;
import com.artsgard.sociobank.dto.AccountTransferDTO;
import com.artsgard.sociobank.model.Account;
import com.artsgard.sociobank.model.AccountTransfer;
import com.artsgard.sociobank.serviceimpl.MapperServiceImpl;
import java.math.BigDecimal;
import java.util.Date;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author WillemDragstra
 */
@ExtendWith(MockitoExtension.class)
public class MapperServiceTest {

    MapperService mapperService = new MapperServiceImpl();
    
    private Account accountMock;
    private Account accountMock2;
    private AccountDTO accountDTOMock;
    private AccountTransfer transferMock;
    private AccountTransferDTO transferDTOMock;

    @BeforeEach
    public void setup() {
        Date now= new Date();
        accountMock = new Account(null, "username1", "iban-x", new BigDecimal("23.56"), "EUR", now, true, null, null);
        accountDTOMock = new AccountDTO(null, "username1", "iban-x", new BigDecimal("23.56"), "EUR", now, true);
        accountMock2 = new Account(null, "username2", "iban-xx", new BigDecimal("53.56"), "USD", now, true, null, null);
        
        transferMock = new AccountTransfer(null, accountMock, accountMock2, new BigDecimal("23.56"), "some first description", now);
        transferDTOMock = new AccountTransferDTO("iban-x", "iban-xx", new BigDecimal("73.56"), "some second description", now);
    }

     @Test
    public void testMapAccountToAccountDTO() {
        assertNotNull(accountMock);
        AccountDTO dto = new AccountDTO();
        dto = mapperService.mapAccountToAccountDTO(accountMock);
        assertThat(dto, isA(AccountDTO.class));
        assertEquals(dto.getUsername(), accountMock.getUsername());
    }
    
    @Test
    public void testMapAccountDTOToAccount() {
        assertNotNull(accountDTOMock);
        Account model = mapperService.mapAccountDTOToAccount(accountDTOMock);
        assertThat(model, isA(Account.class));
        assertEquals(model.getUsername(), accountDTOMock.getUsername());
    }
    
    @Test
    public void  testMapAccountTransferToAccountTransferDTO() {
        assertNotNull(transferMock);
        AccountTransferDTO dto = mapperService.mapAccountTransferToAccountTransferDTO(transferMock);
        dto.setIbanSource(transferMock.getAccountSource().getIban());
        dto.setIbanDestiny(transferMock.getAccountDestiny().getIban());
        assertThat(dto, isA(AccountTransferDTO.class));
        assertEquals(dto.getIbanDestiny(), transferMock.getAccountDestiny().getIban());
    }
    
    @Test
    public void testMapAccountTransferDTOToAccountTransfer() {
        assertNotNull(transferDTOMock);
        AccountTransfer model = mapperService.mapAccountTransferDTOToAccountTransfer(transferDTOMock);
        model.setAccountSource(accountMock);
        model.setAccountDestiny(accountMock2);
        assertThat(model, isA(AccountTransfer.class));
        assertEquals(model.getAccountDestiny().getIban(), transferDTOMock.getIbanDestiny());
    }
}
