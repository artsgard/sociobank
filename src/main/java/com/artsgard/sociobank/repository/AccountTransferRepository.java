package com.artsgard.sociobank.repository;

import com.artsgard.sociobank.model.AccountTransfer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountTransferRepository extends JpaRepository<AccountTransfer, Long> {
    static final String ACCOUNT_TRANSFER_BY_IDS = 
             "SELECT * FROM account_transfer WHERE account_source_id=:accountSourceId and account_destiny_id=:accountDestinyId";
    
    static final String ACCOUNT_TRANSFER_BY_IBAN = 
       "SELECT * FROM account_transfer JOIN account ON account_transfer.account_source_id = account.id where  account.iban =:iban";
    
    static final String ACCOUNT_TRANSFER_BY_USERNAME = 
       "SELECT * FROM account_transfer JOIN account ON account_transfer.account_source_id = account.id where  account.username =:username";
    
    
    
    Optional<AccountTransfer> getAccountTransferById(Long id);
    
    @Query(value = ACCOUNT_TRANSFER_BY_IDS, nativeQuery = true)
    Optional<AccountTransfer> getByAccountSourceIdAndAccountDestinyId(@Param("accountSourceId") Long accountSourceId, @Param("accountDestinyId") Long accountDestinyId);
    
    List<AccountTransfer> findByAccountSourceId(Long accountSourceId);
    
    @Query(value = ACCOUNT_TRANSFER_BY_IBAN, nativeQuery = true)
    List<AccountTransfer> findByIban(@Param("iban") String iban);
    
    @Query(value = ACCOUNT_TRANSFER_BY_USERNAME, nativeQuery = true)
    List<AccountTransfer> findByUsername(@Param("username") String username);
}
