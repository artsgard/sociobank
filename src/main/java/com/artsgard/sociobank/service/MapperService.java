package com.artsgard.sociobank.service;

import com.artsgard.sociobank.dto.AccountDTO;
import com.artsgard.sociobank.dto.AccountTransferDTO;
import com.artsgard.sociobank.model.Account;
import com.artsgard.sociobank.model.AccountTransfer;

public interface MapperService {
    Account mapAccountDTOToAccount(AccountDTO dto);
    AccountDTO mapAccountToAccountDTO(Account ent);
    AccountTransfer mapAccountTransferDTOToAccountTransfer(AccountTransferDTO dto);
    AccountTransferDTO mapAccountTransferToAccountTransferDTO(AccountTransfer ent);
}
