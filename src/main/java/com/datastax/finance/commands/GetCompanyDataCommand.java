package com.datastax.finance.commands;

import java.util.ArrayList;
import java.util.List;

import com.datastax.demo.utils.UrlUtil;
import com.datastax.finance.objects.yahoo.CompanyData;
import com.datastax.finance.objects.yahoo.YahooTradeQuote;

public class GetCompanyDataCommand implements Command{
	
	private List<String> tickers = new ArrayList<String>();
	private List<CompanyData> data;
	
	public void setTickers(List<String> tickers) {
		this.tickers = tickers;
	}

	public List<CompanyData> getCompanyData(){
		return data;
	}

	public List<YahooTradeQuote> getYahooTradeQuotes(){
		
		List<YahooTradeQuote> quotes = new ArrayList<YahooTradeQuote>();
		
		for (CompanyData companyData : data){
			YahooTradeQuote quote = (YahooTradeQuote)companyData;
			
			quotes.add(quote);
		}
		
		return quotes;
	}
		
	
	public void execute(){
		
		for (String ticker : tickers){
			CompanyData companyData = new CompanyData(ticker);
			System.out.println("Url : " + companyData.getUrl());
		
			String csv = UrlUtil.getURLData(companyData.getUrl());
			companyData.parseData(csv);
			this.data.add(companyData);
		}
	}
	
	public static void main (String args[]){
	}
}