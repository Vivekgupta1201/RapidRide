package com.rapidride.locationservice.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rapidride.locationservice.dto.DriverLocationRequest;
import com.rapidride.locationservice.dto.NearByDriverLocationResponse;
import com.rapidride.locationservice.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {
	
	private final LocationService loctionService;
	
	@PostMapping("/driver")
	public ResponseEntity<String> updateDriverLocation(
			@RequestBody DriverLocationRequest driverLocationRequest){
		
		loctionService.updateDriverLocation(driverLocationRequest);
		return ResponseEntity.ok("driver location updated");
	}
	

	/*find the driver in radius of */
	
	@GetMapping("/search")
	public ResponseEntity<List<NearByDriverLocationResponse>> GetAllDrivers (
			@RequestParam double latitude,
			@RequestParam double longitude,
			@RequestParam (defaultValue = "5.0") double radius){
		
		return ResponseEntity.ok(loctionService.findnearByDriver(latitude,longitude, radius));
	}

}
