package com.datastax.demo.utils;
/*
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.text.NumberFormatter;


/**
 */
public class Utilities {

	public final static String LINE_BREAK = "\015\012";
	
	public static void writeStringToFile(String data, String filename, boolean append) throws IOException {
		BufferedWriter br = new BufferedWriter(new PrintWriter(new FileWriter(
		    filename, append)));
		br.write(data);
		br.write(LINE_BREAK);
		br.flush();
		br.close();
	}

	public static void writeListToFile(List data, String filename, boolean append) throws IOException {
		
		File f = new File(filename);
		//System.out.println("Writing to file : " + f.getAbsolutePath());
		BufferedWriter br = new BufferedWriter(new PrintWriter(new FileWriter(f, append)));			
		
		for (int i = 0 ; i < data.size() ; i++){
			Object line = (Object)data.get(i);
			
			if  (line!=null && !line.equals("")){
				br.write(line.toString());			
				br.write(LINE_BREAK);
			}
		}
		
		br.flush();
		br.close();
	}
	
	
	public static String readStringFromFile(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		StringBuffer buffer = new StringBuffer();
		
		String data = br.readLine();
		
		while (data != null){
			buffer.append(data);
			buffer.append("\n");
			data = br.readLine();
		}
	
		br.close();
		
		return buffer.toString();
	}
	
	public static List readStringFromFile(String filename, int noOfLines) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String prices[] = new String[noOfLines];
		ArrayList lines = new ArrayList();
		
		String data = br.readLine();
		int counter = 0;
		
		while (data != null && counter++ < noOfLines){
			
			lines.add(data);
			data = br.readLine();
		}
				
		br.close();
	
		return lines;
	}
	
	public static String stripQuotes(String str){
		return str.replace('\"', ' ').trim();
	}
	
	public Calendar createCalendar(String date, String time){
		Calendar dateTime = GregorianCalendar.getInstance();
		
		return dateTime;
	}
	/*
	public static void sendEmail(String smtpHost, int smtpPort,
	                            String from, String to,
	                            String subject, String content)
	            throws AddressException, MessagingException {
	  
	    //Create a mail session	  
	    java.util.Properties props = new java.util.Properties();
	    props.put("mail.smtp.host", smtpHost);
	    props.put("mail.smtp.port", ""+smtpPort);
	    Session session = Session.getDefaultInstance(props, null);
	
	    // Construct the message
	    Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(from));
	    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
	    msg.setSubject(subject);
	    msg.setText(content);
	
	    // Send the message
	    Transport.send(msg);
	}
	*/
	
    private void printDoubleArray(double[] array){
    	printDoubleArray(array, 1);
    }

    private void printDoubleArray(double[] array, int length){
    	
    	for (int i=0; i < length; i++){
    		System.out.println(new Double(array[i]).intValue() + " ");
    	}
    }
    
    public static void writeToLog(String message){
    	try{
    		Utilities.writeStringToFile(message, "log.txt", true);
    	}catch (Exception e){
    		
    	}
    }

	public static double[] reverseArray(double[] doubles) {

		double[] reverse = new double[doubles.length];
		
		for (int i=0; i < reverse.length; i++){
			
			reverse[i] = doubles[doubles.length-i-1];
		}
		
		return reverse;
		
	}
}
