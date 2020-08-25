package com.artsgard.sociobank.service;

import com.artsgard.sociobank.dto.CurrencyDTO;

public interface ConverterService {
     CurrencyDTO getConvertion(String baseValue, String currencyCode);
}
