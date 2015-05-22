package com.datastax.demo.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class UrlUtil {

	public static String getURLData(String urlString){

		try{
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();     
			connection.connect();               

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer buffer = new StringBuffer();
			String str = in.readLine();

			while (str != null){
				buffer.append(str + "\n");
				str = in.readLine();
			}

			in.close();
			return buffer.toString();

		}catch (Exception e){
			System.out.println("Exception : " + e.getMessage());
		}
		return null;
	}  


	public static List getURLDataList(URL StrurlStringing){

		List list = new ArrayList();

		try{
			
	    	System.out.println("Loading - " + StrurlStringing);
	    	
			URLConnection connection = StrurlStringing.openConnection();     
			connection.connect();               

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String str = in.readLine();

			while (str != null){
				list.add(str);
				str = in.readLine();
			}

			in.close();
			return list;

		}catch (Exception e){
			System.out.print("Error : " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}  	
}
