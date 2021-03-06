package com.datastax.finance.objects;

import java.util.Date;

/**
 * Get the core details for a quote object.
 * @author pcallaghan
 */
public class Quote {

    protected String name;
    protected Date date;
    protected double open;
    protected double high;
    protected double low;
    protected double close;
    protected double volume;
    protected double adjClose;
    	    
    /**
     * Constructor
     */
    public Quote() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @return Returns the adjClose.
     */
    public double getAdjClose() {
        return adjClose;
    }
    /**
     * @param adjClose The adjClose to set.
     */
    public void setAdjClose(double adjClose) {
        this.adjClose = adjClose;
    }
    /**
     * @return Returns the close.
     */
    public double getClose() {
        return close;
    }
    /**
     * @param close The close to set.
     */
    public void setClose(double close) {
        this.close = close;
    }
    /**
     * @return Returns the date.
     */
    public Date getDate() {
        return date;
    }
    /**
     * @param date The date to set.
     */
    public void setDate(Date date) {
        this.date = date;
    }
    /**
     * @return Returns the high.
     */
    public double getHigh() {
        return high;
    }
    /**
     * @param high The high to set.
     */
    public void setHigh(double high) {
        this.high = high;
    }
    /**
     * @return Returns the low.
     */
    public double getLow() {
        return low;
    }
    /**
     * @param low The low to set.
     */
    public void setLow(double low) {
        this.low = low;
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
     * @return Returns the open.
     */
    public double getOpen() {
        return open;
    }
    /**
     * @param open The open to set.
     */
    public void setOpen(double open) {
        this.open = open;
    }
    /**
     * @return Returns the volume.
     */
    public double getVolume() {
        return volume;
    }
    /**
     * @param volume The volume to set.
     */
    public void setVolume(long volume) {
        this.volume = volume;
    }

	@Override
	public String toString() {
		return "Quote [name=" + name + ", date=" + date + ", open=" + open + ", high=" + high + ", low=" + low
				+ ", close=" + close + ", volume=" + volume + ", adjClose=" + adjClose + "]";
	}      
    
    
}
