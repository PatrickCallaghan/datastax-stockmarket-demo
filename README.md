StockMarket Demo
========================================================

## Schema Setup
Note : This will drop the keyspace "datastax_stockmarket_demo" and create a new one. All existing data will be lost. 

The schema can be found in src/main/resources/cql/

To specify contact points use the contactPoints command line parameter e.g. '-DcontactPoints=192.168.25.100,192.168.25.101'
The contact points can take mulitple points in the IP,IP,IP (no spaces).

To create the a single node cluster with replication factor of 1 for standard localhost setup, run the following

    mvn clean compile exec:java -Dexec.mainClass="com.datastax.demo.SchemaSetup"


Load the static data using a cqlsh session 

	copy exchange_metadata(exchange, symbol, name) from 'src/main/resources/NASDAQ-tickers.csv';
	
	copy exchange_metadata(exchange, symbol, name) from 'src/main/resources/ftse-tickers.csv';


To load 10000 days for each exchange, run the following (this may take awhile)

	mvn exec:java -Dexec.mainClass="com.datastax.finance.processor.StockMarketLoader" -Dexchange=FTSE -Ddays=10000
	
	mvn exec:java -Dexec.mainClass="com.datastax.finance.processor.StockMarketLoader" -Dexchange=NASDAQ -Ddays=10000
	
Every evening a load should run for just 1 day	

	mvn exec:java -Dexec.mainClass="com.datastax.finance.processor.StockMarketLoader" -Dexchange=FTSE -Ddays=1
	
	mvn exec:java -Dexec.mainClass="com.datastax.finance.processor.StockMarketLoader" -Dexchange=NASDAQ -Ddays=1

To keep the latest prices updating throughout the date, you need to run the Runners

	mvn exec:java -Dexec.mainClass="com.datastax.finance.processor.StockMarketRunner" -Dexchange=FTSE
	
	mvn exec:java -Dexec.mainClass="com.datastax.finance.processor.StockMarketRunner" -Dexchange=NASDAQ

	
To run the web server 

    mvn jetty:run
    
This will start the server on the localhost at port 8080.

##Using

Now we can look at our data through our rest interface

http://localhost:8080/datastax-stockmarket-demo/rest/get/companydata/metadata?exchange=NASDAQ&ticker=NFLX

http://localhost:8080/datastax-stockmarket-demo/rest/get/timeseries?exchange=FTSE&ticker=BA.L

http://localhost:8080/datastax-stockmarket-demo/rest/get/timeseries/bytime?exchange=FTSE&ticker=BA.L&start=1198195200000&end=1216076400000

http://localhost:8080/datastax-stockmarket-demo/rest/get/candlestick/timeseries?exchange=FTSE&ticker=BA.L&start=1198195200000&end=1201076400000


http://localhost:8080/datastax-stockmarket-demo/rest/get/biggestchangers?exchange=NASDAQ

http://localhost:8080/datastax-stockmarket-demo/rest/get/companydata/metadata?exchange=NASDAQ&ticker=NFLX





To remove the tables and the schema, run the following.

    mvn exec:java -Dexec.mainClass="com.datastax.demo.SchemaTeardown"
	
