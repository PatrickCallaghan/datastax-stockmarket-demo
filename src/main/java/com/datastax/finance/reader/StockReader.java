/*
 */
package com.datastax.finance.reader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.demo.utils.UrlUtil;
import com.datastax.demo.utils.Utilities;
import com.datastax.finance.objects.TradeQuote;
import com.datastax.refdata.model.HistoricData;

/**
 * Reads a url and creates a List of TradeQuotes
 */
public class StockReader {

	private Logger logger = LoggerFactory.getLogger(StockReader.class);
	private DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm");
    private DateFormat printFormatter = new SimpleDateFormat("yyyy-MM-dd");  

	private final static int DAILY_STOCK_CODE_COLUMN = 0;
	private final static int DAILY_CURRENT_PRICE_COLUMN = 1;
	private final static int DAILY_DATE_COLUMN = 2;
	private final static int DAILY_TIME_COLUMN = 3;
	private final static int DAILY_CHANGE_COLUMN = 4;
	private final static int DAILY_DAY_START_COLUMN = 5;
	private final static int DAILY_DAY_MAX_COLUMN = 6;
	private final static int DAILY_DAY_MIN_COLUMN = 7;
	private final static int DAILY_VOLUME_COLUMN = 8;

	// Date Open High Low Close Volume Adj. Close*
	// 13-Feb-06 646 655.5 646 651.5 47044608 651.5
	private final static int HISTORICAL_DATE_COLUMN = 0;
	private final static int HISTORICAL_OPEN_COLUMN = 1;
	private final static int HISTORICAL_HIGH_COLUMN = 2;
	private final static int HISTORICAL_LOW_COLUMN = 3;
	private final static int HISTORICAL_CLOSE_COLUMN = 4;
	private final static int HISTORICAL_VOLUME_COLUMN = 5;
	private final static int HISTORICAL_ADJ_CLOSE_COLUMN = 6;

	private String urlTQ = "http://download.finance.yahoo.com/d/quotes.csv?f=sl1d1t1c1ohgv&e=.csv&s=";
	private String urlDaily = "http://ichart.finance.yahoo.com/table.csv?";
			
	public List<TradeQuote> getDailyTradeQuotes(List<String> names) throws Exception {
		
		List<TradeQuote> tradeQuotes = new ArrayList<TradeQuote>();
		
		if (names.size() > 100){
			
			int batches = names.size()/100; 
			
			for (int i = 0; i < batches; i ++){
				tradeQuotes.addAll(getDailyTradeQuotesInternal(names.subList(i*100, (i+1)*100)));
			}
			
			return tradeQuotes;
		}else{
			
			return getDailyTradeQuotesInternal(names);
		}
	}
	
	
	
	public List<TradeQuote> getDailyTradeQuotesInternal(List<String> names) throws Exception {
		try {
			List<TradeQuote> tradeQuotes = new ArrayList<TradeQuote>();			
			
			String all = "";
			for (String name : names){
				all = all + name + "+";
			}			
			all = all.substring(0, all.lastIndexOf("+"));

			String url = urlTQ + all;
			System.out.println(url);
			
			// Get the data from the URL and write it to the file.
			String data = UrlUtil.getURLData(url);

			if (data == null)
				return null;

			String[] linesArray = data.split("\n");

			for (int i = 0; i < linesArray.length; i++) {

				data = linesArray[i];

				if (data == null)
					continue;

				String[] dataArray = data.split(",");
				TradeQuote tradeQuote = new TradeQuote();
				tradeQuote.setName(Utilities.stripQuotes(dataArray[DAILY_STOCK_CODE_COLUMN]));

				try {
					tradeQuote.setPrice(new Double(dataArray[DAILY_CURRENT_PRICE_COLUMN]));
				} catch (Exception e) {
				}

				try {
					tradeQuote.setChange(new Double(dataArray[DAILY_CHANGE_COLUMN]));
				} catch (Exception e) {
				}

				try {
					tradeQuote.setTodaysStart(new Double(dataArray[DAILY_DAY_START_COLUMN]));
				} catch (Exception e) {
				}

				try {
					tradeQuote.setDaysMax(new Double(dataArray[DAILY_DAY_MAX_COLUMN]));
				} catch (Exception e) {
				}

				try {
					tradeQuote.setDaysMin(new Double(dataArray[DAILY_DAY_MIN_COLUMN]));
				} catch (Exception e) {
				}

				try {
					tradeQuote.setVolume(new Long(dataArray[DAILY_VOLUME_COLUMN]));
				} catch (Exception e) {
				}

				try {
					String time = Utilities.stripQuotes(dataArray[DAILY_TIME_COLUMN]);					
					String date = Utilities.stripQuotes(dataArray[DAILY_DATE_COLUMN]);
					
					tradeQuote.setDate(dateFormatter.parse(date + " " + time));
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Not sure if I need the date and time for the moment.
				tradeQuotes.add(tradeQuote);
			}

			return tradeQuotes;

		} catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	public List<HistoricData> getHistoricalTradeQuotes(String exchange, String ticker, int days) throws Exception {
		try {
			
			DateTime now = DateTime.now();
			DateTime date = now.minusDays(days);
			
			int a = date.getDayOfMonth();
			int b = date.getMonthOfYear();
			int c = date.getYear();			
			
			String url = urlDaily + "a=" + a + "&b=" + b + "&c" + c + "&s=" + ticker;
			
			List<HistoricData> historicDataList = new ArrayList<HistoricData>();

			// Get the data from the URL and write it to the file.
			logger.info(url);
			String data = UrlUtil.getURLData(url);
			
			if (data == null)
				return historicDataList;

			String[] linesArray = data.split("\n");

			for (int i = 0; i < linesArray.length; i++) {

				data = linesArray[i];

				// Don't want the header details.
				if (data == null || i == 0)
					continue;

				String[] dataArray = data.split(",");
				HistoricData historicData = new HistoricData();
				historicData.setSymbol(ticker);
				historicData.setExchange(exchange);

				try {
					historicData.setDate(printFormatter.parse(new String(dataArray[HISTORICAL_DATE_COLUMN])));
				} catch (Exception e) {
				}

				try {
					historicData.setOpen(new Double(dataArray[HISTORICAL_OPEN_COLUMN]).doubleValue());
				} catch (Exception e) {
				}

				try {
					historicData.setHigh(new Double(dataArray[HISTORICAL_HIGH_COLUMN]).doubleValue());
				} catch (Exception e) {
				}

				try {
					historicData.setLow(new Double(dataArray[HISTORICAL_LOW_COLUMN]).doubleValue());
				} catch (Exception e) {
				}

				try {
					historicData.setClose(new Double(dataArray[HISTORICAL_CLOSE_COLUMN]).doubleValue());
				} catch (Exception e) {
				}

				try {
					historicData.setVolume(new Integer(dataArray[HISTORICAL_VOLUME_COLUMN]).intValue());
				} catch (Exception e) {
				}

				try {
					historicData.setAdjClose(new Double(dataArray[HISTORICAL_ADJ_CLOSE_COLUMN]).doubleValue());
				} catch (Exception e) {
				}

				// Not sure if I need the date and time for the moment.
				historicDataList.add(historicData);
			}

			return historicDataList;

		} catch (Exception e) {
			logger.error("Error : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void main(String[] args) {
		StockReader reader = new StockReader();
		List<String> names = new ArrayList<String>();
		names.add("MSFT");
		names.add("AAPL");
		names.add("GOOG");
		
		try {
			List<TradeQuote> dailyTradeQuotes = reader.getDailyTradeQuotes(names);
			
			for (TradeQuote tradeQuote : dailyTradeQuotes){
				System.out.println(tradeQuote);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
//			List<YahooTradeQuote> historicalTradeQuotes = reader.getHistoricalTradeQuotes("AAPL");
//			
//			for (YahooTradeQuote tradeQuote : historicalTradeQuotes){
//				System.out.println(tradeQuote);
//			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
