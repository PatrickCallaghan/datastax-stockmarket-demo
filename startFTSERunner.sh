#!/bin/sh
nohup mvn exec:java -Dexec.mainClass="com.datastax.finance.processor.StockMarketRunner" -Dexchange=FTSE -DcontactPoints=192.168.104.1,192.168.104.2 > FTSE.out 2>&1 
