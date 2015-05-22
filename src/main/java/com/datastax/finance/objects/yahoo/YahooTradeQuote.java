package com.datastax.finance.objects.yahoo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.datastax.finance.objects.Quote;

/**
 * Trades come in from Historical data in the form
 * 
 * 	Date		Open	High	Low		Close	Volume	Adj. Close*
 *	13-Feb-06	646		655.5	646		651.5	47044608	651.5
 *
 * @author pcallaghan
 */
public class YahooTradeQuote extends Quote {

    private String dateFormat = "yyyy-MM-dd";
    private DateFormat printFormatter = new SimpleDateFormat(dateFormat);    
    /**
     * 
     */
    public YahooTradeQuote() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void setDate(String formatterDate){
        
        try{
        	
            super.setDate(printFormatter.parse(formatterDate));
        }catch (ParseException pe){
            System.out.println("ParseException : " + pe.getMessage() + " DATE : " + formatterDate);
            
            //todo Logging 
        }
    }
    
    public String printDate(){
        return printFormatter.format(super.getDate());
    }

    public String toString(){
        
        StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString());
        buffer.append(" Name : " + name);    
        
        if (date!=null)
            buffer.append(" date : " + this.printDate());
        
        buffer.append(" Close : " + close);
        buffer.append(" Volume : " + volume);
        
        return buffer.toString();
    }
 
}
