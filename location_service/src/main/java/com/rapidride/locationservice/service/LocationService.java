package com.rapidride.locationservice.service;



import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.geo.Point;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;


import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import com.rapidride.locationservice.dto.DriverLocationRequest;
import com.rapidride.locationservice.dto.NearByDriverLocationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationService {
	
	private static final String DRIVERS_GEO_KEY="drivers:location";

	private final RedisTemplate<String, String> redisTemplate;
	
	public void updateDriverLocation(DriverLocationRequest driverlocationrequest) {
	     log.info("Updating location for driver: {}", driverlocationrequest.getDriverId());
	     
	     Point driverPoint = new Point(
	               driverlocationrequest.getLongitude(),
	                driverlocationrequest.getLatitude()
	        );
	     redisTemplate.opsForGeo().add(
	    		 DRIVERS_GEO_KEY,
	    		 driverPoint,
	    		 driverlocationrequest.getDriverId()
	    		);
	     log.info("Location updated for driver: {}", driverlocationrequest.getDriverId());
	     
	}
	
	
	
	     
	  public   List<NearByDriverLocationResponse>findnearByDriver(
	    		      double latitude, double longitude, double radisInKm){
	    	Circle SearchInrange =new Circle(
	    			new Point(longitude,latitude),
	    			new Distance(radisInKm, Metrics.KILOMETERS)
	    			
	    			);
	    	
	    	
	    	GeoResults<GeoLocation<String>> driversListGeoResult=
	    			redisTemplate.opsForGeo().radius(
	    					DRIVERS_GEO_KEY, SearchInrange,
	    					RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
	    					.includeCoordinates()
	    					.includeDistance()
	    					.sortAscending()
	    					.limit(15)
	    					);
	    	
	    List<NearByDriverLocationResponse> nearByDriver=new ArrayList<>();
	    
	    if(driversListGeoResult!=null) {
	    	
	    	for(GeoResult<RedisGeoCommands.GeoLocation<String>> result : driversListGeoResult.getContent()) {
	    		
	    		RedisGeoCommands.GeoLocation<String>location=result.getContent();
	    		
	    		nearByDriver.add(
	    				new NearByDriverLocationResponse(
	    						location.getName(),
	    						location.getPoint().getY(),
	    						location.getPoint().getX(),
	    						result.getDistance().getValue()
	    					   ));
	    		
	    		
	    	}
	    		
	    		
	    		
	    }	
	   
		return nearByDriver;
	    		 
	}
	

}
