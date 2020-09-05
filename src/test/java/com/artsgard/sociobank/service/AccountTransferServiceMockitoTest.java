package com.artsgard.sociobank.service;

import com.artsgard.sociobank.dto.AccountDTO;
import com.artsgard.sociobank.exception.ResourceNotFoundException;
import com.artsgard.sociobank.model.Account;
import com.artsgard.sociobank.repository.AccountRepository;
import com.artsgard.sociobank.serviceimpl.AccountServiceImpl;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountTransferServiceMockitoTest {

    @Mock
    private AccountRepository repo;

    @InjectMocks
    AccountServiceImpl service;

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

    public static final Long NON_EXISTING_ID = 7000L;
    public static final Long EXISTING_ID = 1L;
    public static final String EXISTING_USERNAME = "username";
    public static final String NON_EXISTING_USERNAME = "SDFSDFSFSDFSDF";

    @BeforeEach
    public void setup() {
        Date now =  new Date();
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
    }

    @Test
    public void testFindAllAccounts() {
        given(repo.findAll()).willReturn(accounts);
        List<Account> list = repo.findAll();
        assertThat(list).isNotEmpty().hasSize(3);
    }

    @Test
    public void testFindAllAccounts_not_found() {
        List<Account> emptyList = new ArrayList();
        given(repo.findAll()).willReturn(emptyList);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findAllAccounts();
        });
    }

    @Test
    public void testFindAccountById() {
        account1.setId(EXISTING_ID);
        given(repo.findById(any(Long.class))).willReturn(Optional.of(account1));
        given(mapperService.mapAccountToAccountDTO(any(Account.class))).willReturn(accountDTO1);
        AccountDTO sc = service.findAccountById(any(Long.class));
        assertThat(sc).isNotNull();
        assertThat(sc.getUsername()).isEqualTo(accountDTO1.getUsername());
    }

    @Test
    public void testFindAccountById_not_found() {
        given(repo.findById(any(Long.class))).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findAccountById(any(Long.class));
        });
    }

    @Test
    public void testFindAccountByUsername() {
        account1.setId(EXISTING_ID);
        given(repo.findByUsername(EXISTING_USERNAME)).willReturn(Optional.of(account1));
        given(mapperService.mapAccountToAccountDTO(any(Account.class))).willReturn(accountDTO1);
        AccountDTO sc = service.findAccountByUsername(EXISTING_USERNAME);
        assertThat(sc).isNotNull();
        assertThat(sc.getUsername()).isEqualTo(accountDTO1.getUsername());
    }

    @Test
    public void testFindAccountByUsername_not_found() {
        given(repo.findByUsername(NON_EXISTING_USERNAME)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findAccountByUsername(NON_EXISTING_USERNAME);
        });
    }
    
    @Test
    public void testFindAccountByIban() {
        account1.setId(EXISTING_ID);
        given(repo.findByUsername(EXISTING_USERNAME)).willReturn(Optional.of(account1));
        given(mapperService.mapAccountToAccountDTO(any(Account.class))).willReturn(accountDTO1);
        AccountDTO sc = service.findAccountByUsername(EXISTING_USERNAME);
        assertThat(sc).isNotNull();
        assertThat(sc.getUsername()).isEqualTo(accountDTO1.getUsername());
    }

    @Test
    public void testFindAccountByIban_not_found() {
        given(repo.findByUsername(NON_EXISTING_USERNAME)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findAccountByUsername(NON_EXISTING_USERNAME);
        });
    }

    @Test
    public void testSaveAccount() {
        account1.setId(EXISTING_ID);
        given(repo.save(any(Account.class))).willReturn(account1);
        given(mapperService.mapAccountDTOToAccount(any(AccountDTO.class))).willReturn(account1);
        AccountDTO sc = service.saveAccount(accountDTO1);
        assertThat(sc).isNotNull(); // why is this null!!!!!
    }

    @Test
    public void testUpdateAccount() {
        account1.setId(EXISTING_ID);
        accountDTO1.setId(EXISTING_ID);
        given(repo.findById(any(Long.class))).willReturn(Optional.of(account1));
        given(repo.save(any(Account.class))).willReturn(account1);
        given(mapperService.mapAccountDTOToAccount(any(AccountDTO.class))).willReturn(account1);
        
        AccountDTO sc = service.updateAccount(accountDTO1);
        assertThat(sc).isNotNull(); // why is this null!!!!!
    }  

    @Test
    public void updateAccountTest_not_found() {
        given(repo.findById(any(Long.class))).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            accountDTO1.setId(any(Long.class));
            service.updateAccount(accountDTO1);
        });
    }

    @Test
    public void testDeleteAccount() {
        repo.deleteById(EXISTING_ID);
        verify(repo, times(1)).deleteById(eq(EXISTING_ID));
    }

    @Test
    public void testDeleteAccount_not_found() {
        given(repo.findById(any(Long.class))).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteAccountById(any(Long.class));
        });
    }
}
