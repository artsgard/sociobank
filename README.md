# sociobank
A Springboot REST Bank Account Application for Socios

1) General Info About the Socio-Micro-Service-Demo

2) Specific Info Concerning Each Single Application



## General Info ====

The Socio Micro Services Project will consist of about 10 small (backend) Springboot applications, deployed in a Docker Container/ Linux Oracle Virtual Box. SocioRegister is the principal part of a series of four applications called: starter, mock, jpa, socioregister. Together they show a stepwise buildup to a Springboot REST application, which contains use-cases for registering and adding Socios (similar to Facebook). This line of applications goes from an almost empty Springboot shell (starter: one controller method only) to a small but full-fledged REST application: SocioRegister which will be used as a component of our micro-services.

Next you`ll find four other serving applications. The simple SocioWeather, provides a weather-report by city by consulting an external REST-service called Open Weather. SocioBank, permits money transaction between Socios, also consulting an external service for exchange rates. The SocioSecurity, a Cookie/ Token based SpringSecurity (OAUTH2), still has to be written. Finally the SocioDbBatch application is interesting because it will update, on a daily bases, the databases of SocioRegister (socio_db) and SocioBank (soicio_bank_db). The DBs run on MySQL or Postgres.

From SocioRegister-jpa one finds backend-Validation (javax) and REST-Exception Handeling of Spring (RestControllerAdvice).

Testing, in general, will have an important focus and since we are dealing with Spring(boot) there will systematically testing based on five mayor strategies:

	-@ExtendWith(MockitoExtension.class)

	-@ExtendWith(SpringExtension.class) standalone setup (two ways)

	-@ExtendWith(SpringExtension.class) server tests (@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

	-@DataJpaTest wich is database testing on H2

	-Spring Batch testing

Testing is still "work in progress"



## Specific Info SocioBank ====

SocioBank permits banking amung socios. The application contains a small Spring-Batch part and a web-REST-banking-application. The batch part initializes, remotely by a web-service, some bankaccount data and invokes some transactions. The banking part has a few use-cases to create/ update/ delete accounts and to perform some transactions of accounts with different currencies. An external exchange service will provide the api with the current exchange rate.

### The Small Batch Part

The Batch does two things, first it persists data, by reading a csv-file, into a account table (just reading and writting there is no processor action involved). Next, it will perform some transactions, by reading a seccond csv-file, from one existing account to another (the csv files called accounts.scv and transfers.scv you'll find at the root of this project). At the next project SocioDbBatch you'll find more about Batches in general and its challenging subject of Batch-Testing.


### The Banking REST-service

When starting the main of Springboot's SocioBankApplication the app will initialize the socio_bank_db DB which consists of only teo tables: account and account_transfer. The tables do not contain any data.

### The SocioBank Spring Batch Part

Next you type in your browser-window:

	http://localhost:8082/load-db

and the empty database will be filled with some accounts. Additionally the Batch will preform some transactions. Both data is provided by two csv-files, which you find at the root-folder of the project called:

	accounts.csv
	#iban username balance currency creationDate active
	iban-1;wd;80.00;EUR;2020-08-21 16:22:06;true
	etc.
	
	transfers.csv
	#accountId accountTransferId amount description transferDate
	iban-1;iban-2;10.00;first;2020-08-21 16:22:06
	etc.
	
As you may see the account has a currency field. The transfer-action calls an external service to convert currency values based on the actual exchange rate.

### The Banking urls

	get http://localhost:8082/account and transfer will list al accounts/ transfers
	
	post http://localhost:8082/account
	
	{
        	"username": "al",
        	"iban": "iban-1234",
        	"balance": 100.00,
        	"currency": "EUR"
    	}
	
	put: adding a "id": 7, delete adding a path var: http://localhost:8082/account/7
	
	http://localhost:8082/transfer
	{
        	"ibanSource": "iban-2",
        	"ibanDestiny": "iban-3",
        	"amount": 10.00,
        	"description": "work luch of friday"
	}
Other urls you'll find at both controller classes.
	
