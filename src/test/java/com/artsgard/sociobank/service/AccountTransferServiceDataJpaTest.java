package com.artsgard.sociobank.service;

import com.artsgard.sociobank.exception.ResourceNotFoundException;
import com.artsgard.sociobank.model.Account;
import com.artsgard.sociobank.model.AccountTransfer;
import com.artsgard.sociobank.repository.AccountRepository;
import com.artsgard.sociobank.repository.AccountTransferRepository;
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
public class AccountTransferServiceDataJpaTest {

    @Autowired
    private AccountTransferRepository repo;
    
    @Autowired
    private AccountRepository accountRepo;

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
    private Long transferId;
    private Long accountId1;
    private Long accountId2;
    

    @BeforeEach
    public void setup() {


        repo.deleteAll();
        transfers = repo.findAll();
        accounts = accountRepo.findAll();
        Date now = new Date();
        
        account1 = new Account(null, "username1", "iban-x", new BigDecimal("23.56"), "EUR", now, true, null, null);
        account2 = new Account(null, "username2", "iban-xx", new BigDecimal("53.56"), "USD", now, true, null, null);
        account3 = new Account(null, "username3", "iban-xxx", new BigDecimal("73.56"), "USD", now, true, null, null);
        accounts = new ArrayList();
        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
        accountRepo.saveAll(accounts);
        
        transfer1 = new AccountTransfer(null, account1, account2, new BigDecimal("23.56"), "some first description", now);
        transfer2 = new AccountTransfer(null, account2, account3, new BigDecimal("73.56"), "some second description", now);
        transfers = new ArrayList();
        transfers.add(transfer1);
        transfers.add(transfer2);
        
        repo.saveAll(transfers);
        transferId = transfer1.getId();
        accountId1 = account1.getId();
        accountId2 = account2.getId();
        transfers = repo.findAll();
        accounts = accountRepo.findAll();
    }
    
    @Test
    public void testRepo() {
        assertThat(repo).isNotNull();
    }

    @Test
    public void testFindAllAccountTransfers() {
        List<AccountTransfer> trans = repo.findAll();
        assertThat(trans).isNotEmpty();
        assertThat(trans).hasSize(2);
    }

    @Test
    public void testFindAllAccountTransfers_not_found() {
        repo.deleteAll();
        repo.findAll();
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    @Test
    public void testFindAccountTransferById() {
        AccountTransfer trans = repo.getOne(transferId);
        assertThat(trans).isNotNull();
    }

    @Test
    public void testFindAccountTransferById_not_found() {
        repo.getOne(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }
    
    @Test
    public void testFindAccountTransferByIds() {
        Optional<AccountTransfer> trans = repo.getByAccountSourceIdAndAccountDestinyId(accountId1, accountId2);
        assertThat(trans.get()).isEqualTo(transfer1);
    }
    
    @Test
    public void testFindAccountTransferByIds_not_found() {
        repo.getByAccountSourceIdAndAccountDestinyId(transferId, NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    @Test
    public void testFindAccountTransfersByAccountSourceId() {
        List<AccountTransfer> trans = repo.findByAccountSourceId(accountId1);
        assertThat(trans).isNotEmpty();
        assertThat(trans).contains(transfer1);
    }
    
    @Test
    public void testFindAccountTransfersByAccountSourceId_not_found() {
        repo.findByAccountSourceId(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }
    
    @Test
    public void testFindAccountTransfersByIban() {
        List<AccountTransfer> trans = repo.findByIban("iban-x");
        assertThat(trans).isNotEmpty();
        assertThat(trans).contains(transfer1);
    }

    @Test
    public void testFindAccountTransfersByIban_not_found() {
        repo.findByIban(NON_EXISTING_STRING);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }
    
    @Test
    public void testFindAccountTransfersByUsername() {
        List<AccountTransfer> trans = repo.findByUsername("username1");
        assertThat(trans).isNotEmpty();
        assertThat(trans).contains(transfer1);
    }

    @Test
    public void testFindAccountTransfersByUsername_not_found() {
        repo.findByUsername(NON_EXISTING_STRING);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    @Test
    public void saveAccountTransfer() {
        AccountTransfer trans = new AccountTransfer(null, account1, account2, new BigDecimal("100.00"), "some description", new Date());
        repo.save(trans);
        assertThat(trans.getId()).isNotNull();
    }
}
