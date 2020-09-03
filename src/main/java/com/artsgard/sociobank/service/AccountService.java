package com.artsgard.sociobank.service;

import com.artsgard.sociobank.dto.AccountDTO;
import com.artsgard.sociobank.exception.ResourceNotFoundException;
import com.artsgard.sociobank.model.Account;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 
 * @author WillemDragstra
 */
@Service
public interface AccountService  {
    List<AccountDTO> findAllAccounts() throws ResourceNotFoundException;
    AccountDTO findAccountById(Long id) throws ResourceNotFoundException;
    AccountDTO findAccountByUsername(String username) throws ResourceNotFoundException;
    AccountDTO findAccountByIban(String iban) throws ResourceNotFoundException;
    AccountDTO saveAccount(AccountDTO account);
    AccountDTO updateAccount(AccountDTO account) throws ResourceNotFoundException;
    void deleteAccountById(Long id) throws ResourceNotFoundException;
    boolean hasAccountById(Long id);
    boolean hasAccountByIban(String iban);
}