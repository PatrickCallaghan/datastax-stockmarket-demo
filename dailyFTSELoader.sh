#!/bin/sh
nohup mvn exec:java -Dexec.mainClass="com.datastax.finance.processor.StockMarketLoader" -Dexchange=FTSE -Ddays=1 -DcontactPoints=192.168.104.1,192.168.104.2 > FTSE_DailyLoader.out 2>&1 
