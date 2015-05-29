package com.datastax.finance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.demo.utils.UrlUtil;
import com.datastax.finance.objects.TradeQuote;
import com.datastax.finance.objects.yahoo.CompanyData;
import com.datastax.refdata.ReferenceDao;
import com.datastax.refdata.model.CandleStickSeries;
import com.datastax.refdata.model.TimeSeries;

public class StockMarketService {
	
	private Logger logger = LoggerFactory.getLogger(StockMarketService.class);
	private ReferenceDao dao = new ReferenceDao(new String[]{"192.168.104.1"});

	public TimeSeries getTimeSeriesByTickerAndTime(String exchange, String ticker) {
					
		return dao.selectHistoricDataByTickerAndTime(exchange, ticker, 0L, Long.MAX_VALUE);
	}
	
	public TimeSeries getTimeSeriesByTickerAndTime(String exchange, String ticker, long start, long end) {
		
		return dao.selectHistoricDataByTickerAndTime(exchange, ticker, start, end);
	}
	
	public Map<String, String> getCompanyMetaData(String exchange, String ticker){
		return dao.selectMetaDataByExchangeSymbol(exchange, ticker);
	}
	
	public CompanyData getCompanyData(String exchange, String ticker){
		
		CompanyData companyData = new CompanyData(ticker);
		logger.debug("Url : " + companyData.getUrl());
	
		String csv = UrlUtil.getURLData(companyData.getUrl());
		companyData.parseData(csv);
		return companyData;
	}
	
	public List<TradeQuote> getBiggestChangers(String exchange){
				
		List<TradeQuote> list = dao.selectAllLatest(exchange);
		
		Collections.sort(list, new PercentChangeComparator());
		
		if (list.size() > 10){
			List<TradeQuote> results = new ArrayList<TradeQuote>();
			results.addAll(list.subList(0, 5));
			results.addAll(list.subList(list.size()-5, list.size()));
			
			return results;
		}
		return list;	
	}
	class PercentChangeComparator implements Comparator<TradeQuote> {
	    @Override
	    public int compare(TradeQuote a, TradeQuote b) {
	    	if (a == null && b == null) return 0;
	    	if (a == null) return 1;
	    	if (b == null) return -1;	  	    	

	    	if (new Double(a.getPercentChange()).isNaN()) return -1;
	    	if (new Double(b.getPercentChange()).isNaN()) return 1;
	    	if (new Double(a.getPercentChange()).isNaN() && new Double(b.getPercentChange()).isNaN()) return 0;
	    		    	
	    	if (a.getPercentChange() == b.getPercentChange()) return 0;
	    	if (a.getPercentChange() < b.getPercentChange()) return 1;
	    	if (a.getPercentChange() > b.getPercentChange()) return -1;
	    	
	    	return 0;
	    }
	}
	public CandleStickSeries getCandleStickByTickerAndTime(String exchange, String ticker, long start, long end) {
		return dao.selectHLOC(exchange, ticker, start, end);
	}

}
