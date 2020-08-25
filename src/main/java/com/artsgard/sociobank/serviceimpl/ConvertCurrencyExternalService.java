package com.artsgard.sociobank.serviceimpl;

import com.artsgard.sociobank.dto.CurrencyDTO;
import com.artsgard.sociobank.service.ConverterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
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

   /**
    * 
    * @param baseValue
    * @param currencyCode
    * @return 
    */
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

   /**
    * 
    * @param url
    * @return
    * @throws MalformedURLException
    * @throws IOException 
    */
    private HttpURLConnection getConnection(String url) {
       try {
           URL serverAddress = new URL(url);
           
           Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("some-proxy", 8080));
           HttpURLConnection connection;
           try {
               connection = (HttpURLConnection) serverAddress.openConnection(); //openConnection(proxy)
           } catch (IOException ex) {
               logger.error("<Server IOException: " + ex);
               return null;
           }
           
           connection.setDoOutput(true);
           connection.setDoInput(true);
           try {
               connection.setRequestMethod("GET");
           } catch (ProtocolException ex) {
               logger.error("<Server IOException: " + ex);
               return null;
           }
           connection.setRequestProperty("Accept", "application/json");
           connection.setReadTimeout(10000);
           try {
               connection.connect();
           } catch (IOException ex) {
               logger.error("<Server IOException: " + ex);
               return null;
           }
           return connection;
       } catch (MalformedURLException ex) {
           logger.error("<Server IOException: " + ex);
           return null;
       }
    }
}
