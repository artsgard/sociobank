package com.artsgard.sociobank.service;

import com.artsgard.sociobank.dto.AccountDTO;
import com.artsgard.sociobank.dto.AccountTransferDTO;
import com.artsgard.sociobank.exception.ResourceNotFoundException;
import com.artsgard.sociobank.model.Account;
import com.artsgard.sociobank.model.AccountTransfer;
import com.artsgard.sociobank.repository.AccountTransferRepository;
import com.artsgard.sociobank.serviceimpl.AccountTransferServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountTransferServiceMockitoTest {

    @Mock
    private AccountTransferRepository repo;

    @InjectMocks
    AccountTransferServiceImpl service;

    @Mock
    private MapperService mapperService;

    private Account account1;
    private Account account2;
    private Account account3;
    private List<Account> accounts;

    private AccountDTO accountDTO1;
    private AccountDTO accountDTO2;
    private AccountDTO accountDTO3;
    private List<AccountDTO> accountDTOs;

    private AccountTransfer transfer1;
    private AccountTransfer transfer2;
    private List<AccountTransfer> transfers;
    
    private AccountTransferDTO transferDTO1;
    private AccountTransferDTO transferDTO2;
    private List<AccountTransferDTO> transferDTOs;
    private List<AccountTransfer> emptyTransfers = new ArrayList();
    private List<AccountTransferDTO> emptyTransferDTOs = new ArrayList();

    public static final Long NON_EXISTING_ID = 7000L;
    public static final Long NON_EXISTING_ID2 = 7000L;
    public static final Long EXISTING_ID = 1L;
    public static final Long EXISTING_ID2 = 2L;
    public static final String EXISTING_USERNAME_IBAN = "username";
    public static final String NON_EXISTING_USERNAME_IBAN = "SDFSDFSFSDFSDF";

    @BeforeEach
    public void setup() {
        Date now = new Date();
        account1 = new Account(null, "username1", "iban-x", new BigDecimal("23.56"), "EUR", now, true, null, null);
        account2 = new Account(null, "username2", "iban-xx", new BigDecimal("53.56"), "USD", now, true, null, null);
        account3 = new Account(null, "username3", "iban-xxx", new BigDecimal("73.56"), "USD", now, true, null, null);
        accounts = new ArrayList();
        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);

        accountDTO1 = new AccountDTO(null, "username1", "iban-x", new BigDecimal("23.56"), "EUR", now, true);
        accountDTO2 = new AccountDTO(null, "username2", "iban-xx", new BigDecimal("53.56"), "USD", now, true);
        accountDTO3 = new AccountDTO(null, "username3", "iban-xxx", new BigDecimal("73.56"), "USD", now, true);
        accountDTOs = new ArrayList();
        accountDTOs.add(accountDTO1);
        accountDTOs.add(accountDTO2);
        accountDTOs.add(accountDTO3);

        transfer1 = new AccountTransfer(null, account1, account2, new BigDecimal("23.56"), "some first description", now);
        transfer2 = new AccountTransfer(null, account2, account3, new BigDecimal("73.56"), "some second description", now);
        transfers = new ArrayList();
        transfers.add(transfer1);
        transfers.add(transfer2);
        
        transferDTO1 = new AccountTransferDTO("iban-x", "iban-xx", new BigDecimal("23.56"), "some first description", now);
        transferDTO2 = new AccountTransferDTO("iban-xx", "iban-xxx", new BigDecimal("73.56"), "some second description", now);
        transferDTOs = new ArrayList();
        transferDTOs.add(transferDTO1);
        transferDTOs.add(transferDTO2);
    }

    @Test
    public void  testFindAllAccountTransfers() {
        given(repo.findAll()).willReturn(transfers);
        List<AccountTransfer> list = repo.findAll();
        assertThat(list).isNotEmpty().hasSize(2);
    }

    @Test
    public void  testFindAllAccountTransfers_not_found() {
        List<AccountTransfer> emptyList = new ArrayList();
        given(repo.findAll()).willReturn(emptyList);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findAllAccountTransfers();
        });
    }

    @Test
    public void testFindAccountTransferById() {
        transfer1.setId(EXISTING_ID);
        given(repo.findById(EXISTING_ID)).willReturn(Optional.of(transfer1));
        given(mapperService.mapAccountTransferToAccountTransferDTO(any(AccountTransfer.class))).willReturn(transferDTO1);
        given(mapperService.mapAccountToAccountDTO(any(Account.class))).willReturn(accountDTO1);
        AccountTransferDTO tr = service.findAccountTransferById(EXISTING_ID);
        assertThat(tr).isNotNull();
        assertThat(tr.getIbanDestiny()).isEqualTo(transferDTO1.getIbanDestiny());
    }

    @Test
    public void testFindAccountTransferById_not_found() {
        given(repo.findById(NON_EXISTING_ID)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findAccountTransferById(NON_EXISTING_ID);
        });
    }
    
    @Test
    public void testFindAccountTransferByIds() {
        account1.setId(EXISTING_ID);
        given(repo.getByAccountSourceIdAndAccountDestinyId(EXISTING_ID, EXISTING_ID2)).willReturn(Optional.of(transfer1));
        given(mapperService.mapAccountTransferToAccountTransferDTO(any(AccountTransfer.class))).willReturn(transferDTO1);
        AccountTransferDTO tr = service.findAccountTransferByIds(EXISTING_ID, EXISTING_ID2);
        assertThat(tr).isNotNull();
        assertThat(tr.getIbanDestiny()).isEqualTo(transferDTO1.getIbanDestiny());
    }

    @Test
    public void testFindAccountTransferByIds_not_found() {
        given(repo.getByAccountSourceIdAndAccountDestinyId(NON_EXISTING_ID, NON_EXISTING_ID2)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findAccountTransferByIds(NON_EXISTING_ID, NON_EXISTING_ID2);
        });
    }
    
    @Test
    public void testFindAccountTransfersByAccountSourceId() {
        account1.setId(EXISTING_ID);
        given(repo.findByAccountSourceId(any(Long.class))).willReturn(transfers);
        given(mapperService.mapAccountTransferToAccountTransferDTO(any(AccountTransfer.class))).willReturn(transferDTO1);
        List<AccountTransferDTO> trs = service.findAccountTransfersByAccountSourceId(any(Long.class));
        assertThat(trs).isNotEmpty();
        assertThat(trs).contains(transferDTO1);
    }

    @Test
    public void testFindAccountTransfersByAccountSourceId_not_found() {
        given(repo.findByAccountSourceId(NON_EXISTING_ID)).willReturn(emptyTransfers);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findAccountTransfersByAccountSourceId(NON_EXISTING_ID);
        });
    }

    @Test
    public void testFindAccountTransfersByUsername() {
        account1.setId(EXISTING_ID);
        given(repo.findByUsername(EXISTING_USERNAME_IBAN)).willReturn(transfers);
        given(mapperService.mapAccountTransferToAccountTransferDTO(any(AccountTransfer.class))).willReturn(transferDTO1);
        List<AccountTransferDTO> trns = service.findAccountTransfersByUsername(EXISTING_USERNAME_IBAN);
        assertThat(trns).isNotEmpty();
        assertThat(trns).contains(transferDTO1);
    }

    @Test
    public void testFindAccountTransfersByUsername_not_found() {
        
        given(repo.findByUsername(NON_EXISTING_USERNAME_IBAN)).willReturn(emptyTransfers);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findAccountTransfersByUsername(NON_EXISTING_USERNAME_IBAN);
        });
    }

    @Test
    public void testFindAccountTransfersByIban() {
        given(repo.findByIban(EXISTING_USERNAME_IBAN)).willReturn(transfers);
        given(mapperService.mapAccountTransferToAccountTransferDTO(any(AccountTransfer.class))).willReturn(transferDTO1);
        List<AccountTransferDTO> trns = service.findAccountTransfersByIban(EXISTING_USERNAME_IBAN);
        assertThat(trns).isNotEmpty();
        assertThat(trns).contains(transferDTO1);
    }

    @Test
    public void testFindAccountTransfersByIban_not_found() {
        given(repo.findByIban(NON_EXISTING_USERNAME_IBAN)).willReturn(emptyTransfers);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findAccountTransfersByIban(NON_EXISTING_USERNAME_IBAN);
        });
    }

    @Test
    public void testSaveAccountTransfer() {
        transfer1.setId(EXISTING_ID);
        given(repo.save(any(AccountTransfer.class))).willReturn(transfer1);
        given(mapperService.mapAccountTransferDTOToAccountTransfer(any(AccountTransferDTO.class))).willReturn(transfer1);
        AccountTransferDTO tr = service.saveAccountTransfer(transferDTO1);
        assertThat(tr).isNotNull(); // why is this null!!!!!
    }
}
