package com.datastax.refdata;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cern.colt.list.DoubleArrayList;
import cern.colt.list.LongArrayList;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.datastax.finance.objects.TradeQuote;
import com.datastax.refdata.model.CandleStick;
import com.datastax.refdata.model.CandleStickSeries;
import com.datastax.refdata.model.Dividend;
import com.datastax.refdata.model.HistoricData;
import com.datastax.refdata.model.TimeSeries;

public class ReferenceDao {

	private static Logger logger = LoggerFactory.getLogger(ReferenceDao.class);

	private AtomicLong TOTAL_POINTS = new AtomicLong(0);
	private Session session;
	
	private static String keyspaceName = "datastax_stockmarket_demo";
	private static String tableNameHistoric = keyspaceName + ".historic_data";
	private static String tableNameDividends = keyspaceName + ".dividends";
	private static String tableNameMetaData = keyspaceName + ".exchange_metadata";
	private static String tableNameLatest = keyspaceName + ".latest_prices";

	private static final String INSERT_INTO_HISTORIC = "Insert into " + tableNameHistoric
			+ " (exchange,symbol,date,open,high,low,close,volume,adj_close) values (?,?,?,?,?,?,?,?,?);";
	private static final String INSERT_INTO_DIVIDENDS = "Insert into " + tableNameDividends
			+ " (exchange,symbol,date,dividend) values (?,?,?,?);";
	private static final String INSERT_INTO_METADATA = "Insert into " + tableNameMetaData
			+ " (exchange,symbol,price, last_updated_date) values (?,?,?,?);";
	private static final String INSERT_INTO_LATEST = "Insert into " + tableNameLatest
			+ " (exchange,symbol,date,price, change, percentChange, todaysStart, daysMin, daysMax, volume) values (?,?,?,?,?,?,?,?,?,?);";

	private static final String SELECT_ALL = "select * from " + tableNameHistoric;
	private static final String SELECT_ALL_LATEST = "select * from " + tableNameLatest + " where exchange = ?";
	private static final String SELECT_ALL_BY_KEY_AND_DATE = "select * from " + tableNameHistoric
			+ " where exchange=? and symbol=? and date >= ? and date < ?";
	
	private static final String SELECT_TICKERS_BY_EXCHANGE= "select symbol from " + tableNameMetaData + " where exchange=?";
	private static final String SELECT_TICKERS_BY_EXCHANGE_AND_SYMBOL = "select name, last_updated_date, price from " + tableNameMetaData + " where exchange=? and symbol=?";

	private PreparedStatement insertStmtHistoric;
	private PreparedStatement insertStmtDividend;
	private PreparedStatement insertStmtMetaData;
	private PreparedStatement selectStmtByKeyAndDate;
	private PreparedStatement insertStmtLatest;
	private PreparedStatement selectStmtLatest;
	private PreparedStatement selectStmtTickersByExchange;
	private PreparedStatement selectStmtTickersByExchangeSymbol;

	private AtomicInteger requestCount = new AtomicInteger(0);

	public ReferenceDao(String[] contactPoints) {

		Cluster cluster = Cluster.builder().addContactPoints(contactPoints).build();
		this.session = cluster.connect();

		this.insertStmtHistoric = session.prepare(INSERT_INTO_HISTORIC);
		this.insertStmtDividend = session.prepare(INSERT_INTO_DIVIDENDS);
		this.insertStmtMetaData = session.prepare(INSERT_INTO_METADATA);
		this.insertStmtLatest = session.prepare(INSERT_INTO_LATEST);
		
		this.selectStmtLatest = session.prepare(SELECT_ALL_LATEST);
		this.selectStmtByKeyAndDate = session.prepare(SELECT_ALL_BY_KEY_AND_DATE);
		this.selectStmtTickersByExchange = session.prepare(SELECT_TICKERS_BY_EXCHANGE);
		this.selectStmtTickersByExchangeSymbol = session.prepare(SELECT_TICKERS_BY_EXCHANGE_AND_SYMBOL);

		this.insertStmtHistoric.setConsistencyLevel(ConsistencyLevel.ONE);
		this.insertStmtDividend.setConsistencyLevel(ConsistencyLevel.ONE);
		this.insertStmtMetaData.setConsistencyLevel(ConsistencyLevel.ONE);
		this.insertStmtLatest.setConsistencyLevel(ConsistencyLevel.ONE);
		
		this.selectStmtLatest.setConsistencyLevel(ConsistencyLevel.ONE);
		this.selectStmtByKeyAndDate.setConsistencyLevel(ConsistencyLevel.ONE);
		this.selectStmtTickersByExchange.setConsistencyLevel(ConsistencyLevel.ONE);
		this.selectStmtTickersByExchangeSymbol.setConsistencyLevel(ConsistencyLevel.ONE);	

	}

	public void insertHistoricData(List<HistoricData> list) throws Exception {
		BoundStatement boundStmt = new BoundStatement(this.insertStmtHistoric);
		List<ResultSetFuture> results = new ArrayList<ResultSetFuture>();

		for (HistoricData historicData : list) {

			boundStmt.setString("exchange", historicData.getExchange());
			boundStmt.setString("symbol", historicData.getSymbol());
			boundStmt.setDate("date", historicData.getDate());
			boundStmt.setDouble("open", historicData.getOpen());
			boundStmt.setDouble("low", historicData.getLow());
			boundStmt.setDouble("high", historicData.getHigh());
			boundStmt.setDouble("close", historicData.getClose());
			boundStmt.setInt("volume", historicData.getVolume());
			boundStmt.setDouble("adj_close", historicData.getAdjClose());

			results.add(session.executeAsync(boundStmt));
		}

		// Wait till we have everything back.
		for (ResultSetFuture future : results) {
			future.getUninterruptibly();
		}
		
		return;
	}

	public void insertLatestPrices(List<TradeQuote> list, String exchange) throws Exception {

		BoundStatement boundStmt = new BoundStatement(this.insertStmtLatest);
		BoundStatement boundMetaDataStmt = new BoundStatement(this.insertStmtMetaData);
		List<ResultSetFuture> results = new ArrayList<ResultSetFuture>();

		for (TradeQuote tradeQuote : list) {

			boundStmt.setString("exchange", exchange);
			boundStmt.setString("symbol", tradeQuote.getName());
			boundStmt.setDate("date", tradeQuote.getDate());
			boundStmt.setDouble("change", tradeQuote.getChange());
			boundStmt.setDouble("percentChange", getPercentChange(tradeQuote.getChange(), tradeQuote.getTodaysStart()));
			boundStmt.setDouble("price", tradeQuote.getPrice());
			boundStmt.setDouble("todaysStart", tradeQuote.getTodaysStart());
			boundStmt.setDouble("daysMin", tradeQuote.getDaysMin());
			boundStmt.setDouble("daysMax", tradeQuote.getDaysMax());
			boundStmt.setDouble("volume", tradeQuote.getVolume());

			results.add(session.executeAsync(boundStmt));

			boundMetaDataStmt.setString("exchange", exchange);
			boundMetaDataStmt.setString("symbol", tradeQuote.getName());
			boundMetaDataStmt.setDouble("price", tradeQuote.getPrice());
			boundMetaDataStmt.setDate("last_updated_date", tradeQuote.getDate());
			results.add(session.executeAsync(boundMetaDataStmt));
		}

		for (ResultSetFuture future : results) {
			future.getUninterruptibly();
		}
		return;
	}

	private double getPercentChange(double change, double todaysStart) {
		
		if (change == 0  || todaysStart ==0) {
			return 0;
		}else
			return change * 100 / todaysStart;
	}

	public void insertDividend(List<Dividend> list) {
		BoundStatement boundStmt = new BoundStatement(this.insertStmtDividend);
		List<ResultSetFuture> results = new ArrayList<ResultSetFuture>();

		for (Dividend dividend : list) {

			boundStmt.setString("exchange", dividend.getExchange());
			boundStmt.setString("symbol", dividend.getSymbol());
			boundStmt.setDate("date", dividend.getDate());
			boundStmt.setDouble("dividend", dividend.getDividend());

			results.add(session.executeAsync(boundStmt));
		}

		// Wait till we have everything back.
		boolean wait = true;
		while (wait) {
			wait = false;
			for (ResultSetFuture result : results) {
				if (!result.isDone()) {
					wait = true;
					break;
				}
			}
		}
		return;
	}

	public void selectAllHistoricData(int fetchSize) {
		Statement stmt = new SimpleStatement(SELECT_ALL);
		stmt.setFetchSize(fetchSize);
		ResultSet rs = session.execute(stmt);

		Iterator<Row> iterator = rs.iterator();

		while (iterator.hasNext()) {
			iterator.next().getDouble("close");
		}
	}

	public List<String> selectTickersByExchange(String exchange) {

		List<String> symbols = new ArrayList<String>();
		BoundStatement bound = new BoundStatement(selectStmtTickersByExchange);

		ResultSet rs = session.execute(bound.bind(exchange));
		Iterator<Row> iterator = rs.iterator();

		while (iterator.hasNext()) {
			symbols.add(iterator.next().getString("symbol"));
		}
		return symbols;
	}

	public Map<String,String> selectMetaDataByExchangeSymbol(String exchange, String symbol) {

		Map<String,String> map = new HashMap<String, String>();
		BoundStatement bound = new BoundStatement(selectStmtTickersByExchangeSymbol);

		ResultSet rs = session.execute(bound.bind(exchange, symbol));
		Iterator<Row> iterator = rs.iterator();

		while (iterator.hasNext()) {
			Row row = iterator.next();
			
			map.put("name", row.getString("name"));
			map.put("last_update_date", row.getDate("last_updated_date").toString());
			map.put("price", DecimalFormat.getNumberInstance().format(row.getDouble("price")));
			
		}
		return map;
	}
	
	

	public List<TradeQuote> selectAllLatest(String exchange) {
		List<TradeQuote> tradeQuotes = new ArrayList<TradeQuote>(); 

		BoundStatement bound = new BoundStatement(selectStmtLatest);

		ResultSet results = session.execute(bound.bind(exchange));

		for (Row row : results.all()) {
			
			TradeQuote quote = rowToTradeQuote(row);
			
			if (!new Double(quote.getPercentChange()).isNaN())
				tradeQuotes.add(quote);			
		}
		
		return tradeQuotes;
	}
	
	
	private TradeQuote rowToTradeQuote(Row row) {
		
		TradeQuote quote = new TradeQuote();
		quote.setChange(row.getDouble("change"));
		quote.setPercentChange(row.getDouble("percentChange"));
		quote.setDate(row.getDate("date"));
		quote.setDaysMax(row.getDouble("daysMax"));
		quote.setDaysMin(row.getDouble("daysMin"));
		quote.setName(row.getString("symbol"));
		quote.setPrice(row.getDouble("price"));
		quote.setTodaysStart(row.getDouble("todaysStart"));
		quote.setVolume(new Double(row.getDouble("volume")).longValue());
		
		return quote;
	}

	public TimeSeries selectHistoricDataByTickerAndTime(String exchange, String ticker, long start, long end) {

		BoundStatement boundStmt = this.selectStmtByKeyAndDate.bind(exchange, ticker, new Date(start), new Date(end));

		logger.info("" +  new Date(start) + " - " + new Date(end));
		ResultSet rs = session.execute(boundStmt);
				
		List<Row> all = rs.all();
				
		DoubleArrayList valueArray = new DoubleArrayList(20000);
		LongArrayList dateArray = new LongArrayList(20000);
		
		for (Row row : all){
			dateArray.add(row.getDate("date").getTime());
			valueArray.add(row.getDouble("close"));	
		}
		
		dateArray.trimToSize();
		valueArray.trimToSize();
		
		return new TimeSeries(ticker, dateArray.elements(), valueArray.elements());
	}

	public CandleStickSeries selectHLOC(String exchange, String ticker, long start, long end) {
				
		BoundStatement boundStmt = this.selectStmtByKeyAndDate.bind(exchange, ticker, new Date(start), new Date(end));
		CandleStickSeries series = new CandleStickSeries(ticker);
		
		logger.info("" +  new Date(start) + " - " + new Date(end));
		ResultSet rs = session.execute(boundStmt);
				
		List<Row> all = rs.all();
		
		for (Row row : all){
			CandleStick stick = new CandleStick(row.getDouble("high"), row.getDouble("low"), row.getDouble("open"), row.getDouble("close"));
			stick.setStartTime(row.getDate("date").getTime());
			
			series.addCandleStick(stick);
		}
		
		return series;
	}
	
	public long getTotalPoints() {
		return TOTAL_POINTS.get();
	}

	public int getRequestCount() {
		return this.requestCount.get();
	}

}
