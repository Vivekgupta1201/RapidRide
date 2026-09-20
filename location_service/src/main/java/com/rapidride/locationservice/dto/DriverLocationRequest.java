package com.rapidride.locationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DriverLocationRequest {
	private String driverId;
	private double latitude;
	private double longitude;
	

}
