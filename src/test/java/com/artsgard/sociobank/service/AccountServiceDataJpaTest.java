package com.artsgard.sociobank.service;

import com.artsgard.sociobank.exception.ResourceNotFoundException;
import com.artsgard.sociobank.model.Account;
import com.artsgard.sociobank.model.AccountTransfer;
import com.artsgard.sociobank.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource({"classpath:application.properties"})

@DataJpaTest
public class AccountServiceDataJpaTest {

    @Autowired
    private AccountRepository repo;

    public static final Long NON_EXISTING_ID = 7000L;
    public static final String NON_EXISTING_STRING = "SDFSDFSFSDFSDF";

    @Mock
    private MapperService mapperService;
    
    private Account account1;
    private Account account2;
    private Account account3;
    private List<Account> accounts;

    private AccountTransfer transfer1;
    private AccountTransfer transfer2;
    private List<AccountTransfer> transfers;
    private Long existingId;

    @BeforeEach
    public void setup() {
        repo.deleteAll();
        repo.findAll();
        Date now = new Date();
        
        account1 = new Account(null, "username1", "iban-x", new BigDecimal("23.56"), "EUR", now, true, null, null);
        account2 = new Account(null, "username2", "iban-xx", new BigDecimal("53.56"), "USD", now, true, null, null);
        account3 = new Account(null, "username3", "iban-xxx", new BigDecimal("73.56"), "USD", now, true, null, null);
        accounts = new ArrayList();
        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
        
        repo.saveAll(accounts);
        existingId = account1.getId();
    }
    
    @Test
    public void testRepo() {
        assertThat(repo).isNotNull();
    }

    @Test
    public void testFindAllAccounts() {
        List<Account> transfers = repo.findAll();
        assertThat(transfers).isNotEmpty();
        assertThat(transfers).hasSize(3);
    }

    @Test
    public void testFindAllAccounts_not_found() {
        repo.deleteAll();
        repo.findAll();
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    @Test
    public void testFindAccountById() {
        Account acc = repo.getOne(existingId);
        assertThat(acc).isNotNull();
        assertThat(acc).isEqualTo(account1);
    }

    @Test
    public void testFindAccountById_not_found() {
        repo.getOne(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }
    
    @Test
    public void testFindAccountByIban() {
        Optional<Account> acc = repo.findByIban("iban-x");
        assertThat(acc).isPresent();
        assertThat(acc.get()).isEqualTo(account1);
    }

    @Test
    public void testFindAccountByIban_not_found() {
        repo.findByIban(NON_EXISTING_STRING);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }
    
    @Test
    public void testFindAccountByUsername() {
        Optional<Account> acc = repo.findByUsername("username1");
        assertThat(acc).isPresent();
        assertThat(acc.get()).isEqualTo(account1);
    }

    @Test
    public void testFindAccountByUsername_not_found() {
        repo.findByUsername(NON_EXISTING_STRING);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    @Test
    public void testSaveAccount() {
        Date now = new Date();
        Account newAccount = new Account(null, "username-new", "iban-new", new BigDecimal("77.56"), "EUR", now, true, null, null);
        repo.save(newAccount);
        assertThat(newAccount.getId()).isNotNull();
    }
    
    @Test
    public void testUpdateAccount() {
        Account acc = repo.getOne(existingId);
        acc.setUsername("edited username");
        repo.save(acc);
        assertThat(acc.getUsername()).isEqualTo("edited username");
    }
    
    @Test
    public void testUpdateAccount_not_found() {
        repo.getOne(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }
    
    @Test
    public void testDeleteAccount() {
        Date now = new Date();
        Account newAccountToDelete = new Account(null, "username-new", "iban-new", new BigDecimal("77.56"), "EUR", now, true, null, null);
        repo.save(newAccountToDelete);
        Long id = newAccountToDelete.getId();
        repo.delete(newAccountToDelete);
        Optional<Account> deletedAccount = repo.findById(id);
        assertThat(deletedAccount.isPresent()).isFalse();
    }
    
    @Test
    public void testDeleteAccount_not_found() {
        repo.getOne(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }
}
