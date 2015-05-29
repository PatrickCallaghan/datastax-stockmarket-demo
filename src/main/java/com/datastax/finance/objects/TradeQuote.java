package com.datastax.finance.objects;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * TradeQuote Object which represents a trade
 */
public class TradeQuote implements java.io.Serializable{

	private static NumberFormat printFormatter = new DecimalFormat("#0.###	");
	
	private String name;
	private double price;
	private Date date;
	private double change;
	private double percentChange; 
	private double todaysStart;
	private double daysMax;
	private double daysMin;
	private long volume;
	
	
	public double getPercentChange() {
		return percentChange;
	}
	public void setPercentChange(double percentChange) {
		this.percentChange = percentChange;
	}
	/**
	 * @return Returns the calendar.
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param calendar The calendar to set.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return Returns the change.
	 */
	public double getChange() {
		return change;
	}
	/**
	 * @param change The change to set.
	 */
	public void setChange(double change) {
		this.change = change;
	}
	/**
	 * @return Returns the daysMax.
	 */
	public double getDaysMax() {
		return daysMax;
	}
	/**
	 * @param daysMax The daysMax to set.
	 */
	public void setDaysMax(double daysMax) {
		this.daysMax = daysMax;
	}
	/**
	 * @return Returns the daysMin.
	 */
	public double getDaysMin() {
		return daysMin;
	}
	/**
	 * @param daysMin The daysMin to set.
	 */
	public void setDaysMin(double daysMin) {
		this.daysMin = daysMin;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the price.
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price The price to set.
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return Returns the todaysStart.
	 */
	public double getTodaysStart() {
		return todaysStart;
	}
	/**
	 * @param todaysStart The todaysStart to set.
	 */
	public void setTodaysStart(double todaysStart) {
		this.todaysStart = todaysStart;
	}
	/**
	 * @return Returns the volume.
	 */
	public long getVolume() {
		
		return volume;
	}
	/**
	 * @param volume The volume to set.
	 */
	public void setVolume(long volume) {
		this.volume = volume;
	}

	
	public String toString() {
		return "TradeQuote [name=" + name + ", price=" + price + ", date=" + date + ", change=" + change
				+ " percentChange=" + percentChange + ", todaysStart=" + todaysStart + ", daysMax=" + daysMax + ", daysMin=" + daysMin + ", volume="
				+ volume + "]";
	}
	public String toChartString() {
		return "[\"" + name +  "\"," + printFormatter.format(price) + ", " + printFormatter.format(change) + ", " + printFormatter.format((price - change)) + "," + printFormatter.format(percentChange) + "]";
	}
}








