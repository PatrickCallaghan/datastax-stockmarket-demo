package com.datastax.finance.processor;

import java.util.List;

import com.datastax.demo.utils.PropertyHelper;
import com.datastax.finance.objects.TradeQuote;
import com.datastax.finance.reader.StockReader;
import com.datastax.refdata.ReferenceDao;

public class StockMarketRunner implements Runnable{

	private String exchange;
	
	public static void main(String args[]){
		
		String contactPointsStr = PropertyHelper.getProperty("contactPoints", "localhost");
		String exchange = PropertyHelper.getProperty("exchange", "FTSE");
		
		Thread t = new Thread(new StockMarketRunner(exchange, contactPointsStr));
		t.start();
	}
	
	private ReferenceDao dao;
	
	public StockMarketRunner(String exchange, String contactPointsStr){
		this.exchange = exchange;
		this.dao = new ReferenceDao(contactPointsStr.split(","));
	}
	
	public void run(){	
		
		StockReader reader = new StockReader();
		
		//Load in all the tickers that we have 
		List<String> tickers = this.dao.selectTickersByExchange(exchange);
		
		while (true){
			try{							
				List<TradeQuote> dailyTradeQuotes = reader.getDailyTradeQuotes(tickers);
				
				dao.insertLatestPrices(dailyTradeQuotes, exchange);
				
				sleep(15000);
			}catch (Exception e){
				e.printStackTrace();
				System.out.println("Caught throwable : " +e.getMessage());
				System.exit(1);
			}
		}			
	}
	
  	private void sleep (long millis){
  		
		try{
			Thread.sleep(millis);
		}catch (InterruptedException e){			
			System.out.println(e.getMessage());
		}  		
  	}	
}
