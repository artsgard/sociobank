package com.artsgard.sociobank.service;

import com.artsgard.sociobank.dto.AccountTransferDTO;
import com.artsgard.sociobank.exception.ResourceNotFoundException;
import com.artsgard.sociobank.model.AccountTransfer;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author WillemDragstra
 */
@Service
public interface AccountTransferService {
    List<AccountTransferDTO> findAllAccountTransfers() throws ResourceNotFoundException;
    AccountTransferDTO findAccountTransferById(Long id)  throws ResourceNotFoundException;
    AccountTransferDTO findAccountTransferByIds(Long accountId, Long accountTransferId) throws ResourceNotFoundException;
    List<AccountTransferDTO> findAccountTransfersByAccountSourceId(Long id) throws ResourceNotFoundException;
    List<AccountTransferDTO> findAccountTransfersByUsername(String username) throws ResourceNotFoundException;
    List<AccountTransferDTO> findAccountTransfersByIban(String iban) throws ResourceNotFoundException;
    AccountTransferDTO saveAccountTransfer(AccountTransferDTO accountDTO) throws ResourceNotFoundException;
    void transactionService(AccountTransfer transfer);
    BigDecimal convertion(String currency1, String currency2);
}
