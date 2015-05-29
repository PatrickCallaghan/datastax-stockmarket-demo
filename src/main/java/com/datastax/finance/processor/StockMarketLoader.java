package com.datastax.finance.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.demo.utils.PropertyHelper;
import com.datastax.finance.reader.StockReader;
import com.datastax.refdata.ReferenceDao;
import com.datastax.refdata.model.HistoricData;

public class StockMarketLoader implements Runnable{
	
	private Logger logger = LoggerFactory.getLogger(StockMarketLoader.class);
	private ReferenceDao dao;	
	private String exchange;
	
	public static void main(String args[]){
			
		Thread t = new Thread(new StockMarketLoader());
		t.start();
	}
	
	public StockMarketLoader(){
		String contactPointsStr = PropertyHelper.getProperty("contactPoints", "192.168.104.1,192.168.104.5");

		this.exchange = PropertyHelper.getProperty("exchange", "FTSE");
		this.dao = new ReferenceDao(contactPointsStr.split(","));
	}
	
	public void run(){	
		
		final String daysStr = PropertyHelper.getProperty("days", "2");
		final int days = Integer.parseInt(daysStr);
		
		StockReader reader = new StockReader();		
		//Load in all the tickers that we have 
		List<String> tickers = this.dao.selectTickersByExchange(exchange);
		
		for (String ticker : tickers){
			try{							
				List<HistoricData> dailyTradeQuotes = reader.getHistoricalTradeQuotes(exchange, ticker, days);
				
				if (dailyTradeQuotes == null){					
					logger.info("Quotes are null for ticker");
				}else{
					dao.insertHistoricData(dailyTradeQuotes);
				}
				sleep(1);
			}catch (Exception e){
				e.printStackTrace();
				System.out.println("Caught throwable : " +e.getMessage());
				System.exit(1);
			}
		}	
		
		System.exit(0);
	}
	
  	private void sleep (long millis){
  		
		try{
			Thread.sleep(millis);
		}catch (InterruptedException e){			
			System.out.println(e.getMessage());
		}  		
  	}	
}
