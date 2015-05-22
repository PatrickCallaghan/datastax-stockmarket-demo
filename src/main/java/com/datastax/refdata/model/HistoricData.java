package com.datastax.refdata.model;

import java.util.Date;

public class HistoricData {

	private String exchange;
	private String symbol;
	private Date date;
	private double open;
	private double high;
	private double low;
	private double close;
	private int volume;
	private double adjClose;
	
	public HistoricData(String exchange, String symbol, Date date, double open, double high, double low, double close,
			int volume, double adjClose) {
		super();
		this.exchange = exchange;
		this.symbol = symbol;
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.adjClose = adjClose;
	}
	public HistoricData() {
	}
	
	
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public void setAdjClose(double adjClose) {
		this.adjClose = adjClose;
	}
	public String getExchange() {
		return exchange;
	}
	public String getSymbol() {
		return symbol;
	}
	public Date getDate() {
		return date;
	}
	public double getOpen() {
		return open;
	}
	public double getHigh() {
		return high;
	}
	public double getLow() {
		return low;
	}
	public double getClose() {
		return close;
	}
	public int getVolume() {
		return volume;
	}
	public double getAdjClose() {
		return adjClose;
	}
	@Override
	public String toString() {
		return "HistoricData [exchange=" + exchange + ", symbol=" + symbol + ", date=" + date + ", open=" + open
				+ ", high=" + high + ", low=" + low + ", close=" + close + ", volume=" + volume + ", adjClose="
				+ adjClose + "]";
	}
}
