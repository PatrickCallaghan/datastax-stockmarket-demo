package com.datastax.finance.objects.yahoo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.datastax.demo.utils.UrlUtil;

public class CompanyData extends YahooTradeQuote{
	
	private static final String NAME="name";
	private static final String ASK="ask";
	private static final String BID="bid";
	private static final String EPS="earningspershare";
	private static final String YEARLOW="yearlow";
	private static final String YEARHIGH="yearhigh";
	private static final String PRICEEARNINGS="priceearnings";
	private static final String AVGVOL="averagevolume";
	private static final String VOLUME="volume";
	private static final String MARKETCAP="marketcap";
	private static final String DIVYIELD="dividendyield";
	private static final String LOW = "low";
	private static final String HIGH = "high";
	private static final String CLOSE = "close";
	private static final String OPEN = "open";
	private static final String TICKER = "ticker";
	
	//http://www.gummy-stuff.org/Yahoo-data.htm
	//http://finance.yahoo.com/d/quotes.csv?s=AAPL&f=nb2b3ejkra2vj1yghl1o
	private static final String tags = "ssb2b3ejkra2vj1yghl1o";
	private static final String startUrl ="http://finance.yahoo.com/d/quotes.csv?s=";
	private static final String endUrl = "&f=" + tags;
		
	private Map<String, String> companyData = new HashMap<String, String>();
	
	private List<String> tickers = new ArrayList<String>();

	public CompanyData(String ticker){
		this.tickers.add(ticker);		
	}

	public CompanyData(List<String> tickers ){
		this.tickers = tickers;		
	}

	public void addData(String dataName, String value){
		companyData.put(dataName, value);
	}
	
	public Map<String, String> getCompanyData() {
		return companyData;
	}

	public void setCompanyData(Map<String, String> companyData) {
		this.companyData = companyData;
	}
	 
	public String getUrl(){
		
		String tickerUrl = "";
		
		for (int i=0; i < tickers.size(); i ++){
			String ticker = tickers.get(i);
			
			tickerUrl = tickerUrl + ticker;
			
			if (i < tickers.size()-1){
				tickerUrl+= "+";
			}
		}
		
		return startUrl + tickerUrl + endUrl;
	}
	
	public void parseData(String csv){
		String data[] = csv.split(",");
		System.out.println(csv);
		
		try{
			if (data[3].equalsIgnoreCase("N/A")) data[3] = "0";
			if (data[4].equalsIgnoreCase("N/A")) data[4] = "0";
			if (data[5].equalsIgnoreCase("N/A")) data[5] = "0";
			if (data[6].equalsIgnoreCase("N/A")) data[6] = "0";
			if (data[7].equalsIgnoreCase("N/A")) data[7] = "0";
			if (data[8].equalsIgnoreCase("N/A")) data[8] = "0";
			if (data[9].equalsIgnoreCase("N/A")) data[9] = "0";
			if (data[10].equalsIgnoreCase("N/A")) data[10] = "0";
			if (data[11].equalsIgnoreCase("N/A")) data[11] = "0";
			if (data[12].equalsIgnoreCase("N/A")) data[12] = "0";
			if (data[13].equalsIgnoreCase("N/A")) data[13] = "0";
			if (data[14].equalsIgnoreCase("N/A")) data[14] = "0";
			if (data[15].equalsIgnoreCase("N/A")) data[15] = "0";
			
			
			//Check for Millions
			if (data[8].substring(data[8].length()-1).equalsIgnoreCase("M")){
				double d = new Double(data[8].substring(0, data[8].length()-1)).doubleValue();
				
				data[8] = new Double(d*1000000).toString();
			}
			if (data[9].substring(data[9].length()-1).equalsIgnoreCase("M")){
				double d = new Double(data[9].substring(0, data[9].length()-1)).doubleValue();
				
				data[9] = new Double(d*1000000).toString();
			}
			if (data[10].substring(data[10].length()-1).equalsIgnoreCase("M")){
				double d = new Double(data[10].substring(0, data[10].length()-1)).doubleValue();
				
				data[10] = new Double(d*1000000).toString();
			}			
			
			//Check for Billions
			if (data[8].substring(data[8].length()-1).equalsIgnoreCase("B")){
				double d = new Double(data[8].substring(0, data[8].length()-1)).doubleValue();
				
				data[8] = new Double(d*1000000000).toString();
			}
			if (data[9].substring(data[9].length()-1).equalsIgnoreCase("B")){
				double d = new Double(data[9].substring(0, data[9].length()-1)).doubleValue();
				
				data[9] = new Double(d*1000000000).toString();
			}
			if (data[10].substring(data[10].length()-1).equalsIgnoreCase("B")){
				double d = new Double(data[10].substring(0, data[10].length()-1)).doubleValue();
				
				data[10] = new Double(d*1000000000).toString();
			}			
			
			companyData.put(TICKER, data[0]);
			companyData.put(NAME, data[1]);
			companyData.put(ASK, data[2]);
			companyData.put(BID, data[3]);
			companyData.put(EPS, data[4]);
			companyData.put(YEARLOW, data[5]);
			companyData.put(YEARHIGH, data[6]);
			companyData.put(PRICEEARNINGS, data[7]);
			companyData.put(AVGVOL, data[8]);
			companyData.put(VOLUME, data[9]);
			companyData.put(MARKETCAP, data[10]);
			companyData.put(DIVYIELD, data[11]);
			companyData.put(LOW, data[12]);
			companyData.put(HIGH, data[13]);
			companyData.put(CLOSE, data[14]);
			companyData.put(OPEN, data[15]);
			
			this.name = new String(data[0].substring(1, data[0].length()-1));
			
			this.volume =  Double.parseDouble(data[8]);
			this.open = Double.parseDouble(data[14]);
			this.close = Double.parseDouble(data[13]);
			this.low = Double.parseDouble(data[11]);
			this.high = Double.parseDouble(data[12]);
			
			this.date = new Date();

		}catch (Exception e){
			System.out.println("Caught Exception : " + e.getMessage() + " for data[0] : " + data[0]);
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		
		CompanyData companyData = new CompanyData("BARC.L");
		System.out.println("Url : " + companyData.getUrl());
		
		String csv = UrlUtil.getURLData(companyData.getUrl());
		companyData.parseData(csv);
		System.out.println(companyData.toString());
	}
}
