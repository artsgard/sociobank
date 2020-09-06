package com.artsgard.sociobank.serviceimpl;

import com.artsgard.sociobank.dto.CurrencyDTO;
import com.artsgard.sociobank.service.ConverterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author artsgard
 */
@Service
public class ConvertCurrencyExternalService implements ConverterService {

    private org.slf4j.Logger logger;

    public ConvertCurrencyExternalService() {
        logger = LoggerFactory.getLogger(ConvertCurrencyExternalService.class);
    }

    private static final String URL_BASE = "https://api.exchangeratesapi.io/latest?base=";

    private CurrencyDTO dto;

    @Override
    public CurrencyDTO getConvertion(String baseValue, String currencyCode) {
        BufferedReader br = null;
        StringBuilder sb;

        HttpURLConnection connection = null;
        try {
            String url = URL_BASE + baseValue + "&symbols=" + currencyCode;
            connection = getConnection(url);
            br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String output;
            sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            ObjectMapper mapper = new ObjectMapper();
            try {
                dto = mapper.readValue(sb.toString(), CurrencyDTO.class);
            } catch (IOException ex) {
                System.err.println("<IOException error: " + ex);
                logger.error("<IOException error: ", ex);
            }

        } catch (IOException ex) {
            System.err.println("<Server IOException: " + ex);
            logger.error("<Server IOException: " + ex);
        } finally {
            connection.disconnect();
            try {

                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                System.err.println("<Server IOException: " + ex);
                logger.error("<Server IOException: " + ex);
            }
        }
        return dto;
    }

    public HttpURLConnection getConnection(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = null;
        urlConnection = create(url);
        return urlConnection;
    }

    HttpURLConnection create(URL url) {
        try {
            return (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(ConvertCurrencyExternalService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
