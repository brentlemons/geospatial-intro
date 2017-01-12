/**
 * 
 */
package com.fltck.demo.geospatial.controller;

import java.util.ArrayList;
import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fltck.demo.geospatial.model.Airport;
import com.fltck.demo.geospatial.model.SearchCriteria;
import com.fltck.demo.geospatial.service.RepositoryService;

/**
 * @author brentlemons
 *
 */
@RestController
@RequestMapping("/airports")
public class AirportController {
	
    @Autowired
	RepositoryService repository;

    private static final Logger logger = LoggerFactory.getLogger(AirportController.class);
    
    @RequestMapping(method = RequestMethod.GET)
	FeatureCollection getAirport() {
		FeatureCollection featureCollection = null;
		
		List<Airport> airports = repository.getAirports();
		
		if (airports.size() > 0) {
			featureCollection = new FeatureCollection();
			for (Airport airport : airports) {
				Feature feature = new Feature();
				feature.setProperty("id", airport.getId());
				feature.setProperty("iataId", airport.getIataId());
				feature.setProperty("name", airport.getName());
				feature.setProperty("city", airport.getCity());
				feature.setProperty("state", airport.getState());
				feature.setProperty("elevation", airport.getElevation());
				feature.setGeometry(airport.getLocation());
				
				featureCollection.add(feature);
			}
		}
		
		return featureCollection;
	}
    
    
	@RequestMapping(method = RequestMethod.POST)
	FeatureCollection getAirport(@RequestBody SearchCriteria searchCriteria) {
		FeatureCollection featureCollection = null;
		
		List<Airport> airports = new ArrayList<Airport>();
		
		if (searchCriteria.getBoundingBox() != null) {
			airports = repository.getAirportsByBoundingBox(searchCriteria);
		} else if (searchCriteria.getDistance() != null) {
			airports = repository.getAirportsByDistance(searchCriteria);
		}
		
		if (airports.size() > 0) {
			featureCollection = new FeatureCollection();
			for (Airport airport : airports) {
				Feature feature = new Feature();
				feature.setProperty("id", airport.getId());
				feature.setProperty("iataId", airport.getIataId());
				feature.setProperty("name", airport.getName());
				feature.setProperty("city", airport.getCity());
				feature.setProperty("state", airport.getState());
				feature.setProperty("elevation", airport.getElevation());
				feature.setGeometry(airport.getLocation());
				
				featureCollection.add(feature);
			}
		}
		
		return featureCollection;
	}

}
