package com.rapidride.locationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearByDriverLocationResponse {
	  private String driverId;
	    private double latitude;
	    private double longitude;
	    private double distanceInKm;

}
