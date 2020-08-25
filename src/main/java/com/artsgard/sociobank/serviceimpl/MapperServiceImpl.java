package com.artsgard.sociobank.serviceimpl;

import com.artsgard.sociobank.dto.AccountDTO;
import com.artsgard.sociobank.dto.AccountTransferDTO;
import com.artsgard.sociobank.model.Account;
import com.artsgard.sociobank.model.AccountTransfer;
import com.artsgard.sociobank.service.AccountService;
import com.artsgard.sociobank.service.MapperService;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.convention.MatchingStrategies;

@Service
public class MapperServiceImpl implements MapperService {

    org.slf4j.Logger logger = LoggerFactory.getLogger(MapperServiceImpl.class);
     
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private AccountService accountService;
    
    public MapperServiceImpl() {
        this.logger = LoggerFactory.getLogger(MapperServiceImpl.class);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public AccountTransfer mapAccountTransferDTOToAccountTransfer(AccountTransferDTO dto) {
        if (dto != null) {
            return modelMapper.map(dto, AccountTransfer.class);
        } else {
            return null;
        }
    }

    @Override
    public AccountTransferDTO mapAccountTransferToAccountTransferDTO(AccountTransfer ent) {
        if (ent != null) {
            return modelMapper.map(ent, AccountTransferDTO.class);
        } else {
            return null;
        }
    }

    @Override
    public Account mapAccountDTOToAccount(AccountDTO dto) {
         if (dto != null) {
            return modelMapper.map(dto, Account.class);
        } else {
            return null;
        }
    }

    @Override
    public AccountDTO mapAccountToAccountDTO(Account ent) {
         if (ent != null) {
            return modelMapper.map(ent, AccountDTO.class);
        } else {
            return null;
        }
    }
}
