package com.artsgard.sociobank.controller;

import com.artsgard.sociobank.dto.AccountDTO;
import com.artsgard.sociobank.service.AccountService;
import com.artsgard.sociobank.service.MapperService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author artsgard
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    org.slf4j.Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private MapperService mapperService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> findAllAccounts() {
        List<AccountDTO> accounts = accountService.findAllAccounts();
        List<AccountDTO> list = new ArrayList();
        accounts.forEach((account) -> {
            list.add(account);
        });
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<AccountDTO> findAccountById(@PathVariable Long id) {
       AccountDTO account = accountService.findAccountById(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping(path = "/iban/{iban}", produces = "application/json")
    public ResponseEntity<?> findAccountByIban(@PathVariable String iban) {
        AccountDTO account = accountService.findAccountByIban(iban);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
    
    @GetMapping(path = "/username/{username}", produces = "application/json")
    public ResponseEntity<?> findAccountByUsername(@PathVariable String username) {
        AccountDTO account = accountService.findAccountByUsername(username);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> saveAccount(@Valid @RequestBody AccountDTO accountDTO) {
        AccountDTO account = accountService.saveAccount(accountDTO);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> updateAccount(@Valid @RequestBody AccountDTO accountDTO) {
        accountService.hasAccountById(accountDTO.getId());       
        return new ResponseEntity<>(accountService.updateAccount(accountDTO), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteAccountById(@PathVariable("id") Long id) {
        accountService.deleteAccountById(id);
        return new ResponseEntity<>(HttpStatus.OK);  
    }
}
