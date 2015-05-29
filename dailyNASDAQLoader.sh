#!/bin/sh
nohup mvn exec:java -Dexec.mainClass="com.datastax.finance.processor.StockMarketLoader" -Dexchange=NASDAQ -Ddays=1 -DcontactPoints=192.168.104.1,192.168.104.2 > NASDAQ_Daily_Loader.out 2>&1 
