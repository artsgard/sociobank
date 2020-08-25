package com.artsgard.sociobank.dto;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CurrencyDTO {
    private Map<String, String> rates = new HashMap<>();
    private String base;
    private Timestamp date;
}
