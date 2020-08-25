package com.artsgard.sociobank.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author wdragstra
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class AccountDTO implements Serializable {

    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String iban;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 12, fraction = 2)
    private BigDecimal balance;

    @NotNull
    private String currency;

    private Date creationDate;

    private Boolean active;
}
