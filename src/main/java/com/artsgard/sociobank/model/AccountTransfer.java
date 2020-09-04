package com.artsgard.sociobank.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author wdragstra
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "AccountTransfer")
@Table(name = "account_transfer")
public class AccountTransfer implements Serializable {

    @Id
    //@Column(name="id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonIgnoreProperties("sourceTransfers")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_source_id", updatable = true, insertable = true,
            referencedColumnName = "id")
    private Account accountSource;
    
    @JsonIgnoreProperties("destinyTransfers")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_destiny_id", updatable = true, insertable = true,
            referencedColumnName = "id")
    private Account accountDestiny;
    
    @Column(name="amount", nullable = false)
    private BigDecimal amount;
  
    @Column(name="description", nullable = true)
    private String description;
    
    @Column(name="transfer_date", nullable = false)
    private Date transferDate;
  
}
