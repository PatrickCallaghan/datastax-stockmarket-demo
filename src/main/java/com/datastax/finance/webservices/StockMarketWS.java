package com.datastax.finance.webservices;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.finance.StockMarketService;
import com.datastax.finance.objects.TradeQuote;
import com.datastax.finance.objects.yahoo.CompanyData;
import com.datastax.refdata.model.CandleStickSeries;
import com.datastax.refdata.model.TimeSeries;

/**
 * Web Service for global service.
 * @author patrickcallaghan
 *
 */
@Path("/")
public class StockMarketWS {

	private Logger logger = LoggerFactory.getLogger(StockMarketWS.class);

	//Service Layer.
	private StockMarketService service = new StockMarketService();

	@GET
	@Path("/get/timeseries")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTickerTimeSeries(@QueryParam("exchange") String exchange, @QueryParam("ticker") String ticker) {

		TimeSeries timeSeries = this.service.getTimeSeriesByTickerAndTime(exchange, ticker);			
		return Response.status(201).entity(timeSeries).build();
	}

	@GET
	@Path("/get/candlestick/timeseries")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTickerTimeSeriesChart(@QueryParam("exchange") String exchange, @QueryParam("ticker") String ticker, @QueryParam("start") long start,
			@QueryParam("end") long end) {

		CandleStickSeries candleStickSeries = this.service.getCandleStickByTickerAndTime(exchange, ticker, start, end);
		
		return Response.status(201).entity(candleStickSeries.toChartString()).build();
	}
	

	@GET
	@Path("/get/timeseries/bytime/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimesSeriesByKey(@QueryParam("exchange") String exchange, @QueryParam("ticker") String ticker, @QueryParam("start") long start,
			@QueryParam("end") long end) {

		TimeSeries timeSeries = this.service.getTimeSeriesByTickerAndTime(exchange, ticker, start, end);
		return Response.status(201).entity(timeSeries).build();
	}
	
	@GET
	@Path("/get/biggestchangers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBiggestChangers(@QueryParam("exchange") String exchange){
		List<TradeQuote> biggestChangers = service.getBiggestChangers(exchange);
		return Response.status(201).entity(biggestChangers).build();
	}
	
	@GET
	@Path("/get/companydata")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompanyData(@QueryParam("exchange") String exchange, @QueryParam("ticker") String ticker){
		CompanyData companyData = service.getCompanyData(exchange, ticker);
		return Response.status(201).entity(companyData).build();
	}

	@GET
	@Path("/get/companydata/metadata")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompanyMetaData(@QueryParam("exchange") String exchange, @QueryParam("ticker") String ticker){
		Map<String, String> metaData = service.getCompanyMetaData(exchange, ticker);
		return Response.status(201).entity(metaData).build();
	}

	@GET
	@Path("/get/testarray")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTetst(){
		Object[] array = new Object[50];
		
		for (int i =0; i < 50; i++){
			Object[] innerArray = new Object[5];
			
			if (i%10==0){
				innerArray[0] = "Some Date";
			}else{
				innerArray[0] = "";
			}
			innerArray[1] = 132;
			innerArray[2] = 133;
			innerArray[3] = 134;
			innerArray[4] = 135;
			
			array[i] = innerArray;			
		}
		
		return Response.status(201).entity(array).build();
	}	
}


