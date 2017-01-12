package com.fltck.demo.geospatial.service;

import java.util.List;

import org.geojson.Point;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fltck.demo.geospatial.model.Airport;
import com.fltck.demo.geospatial.model.Place;
import com.fltck.demo.geospatial.model.SearchCriteria;

public interface RepositoryService {
	public List<Airport> getAirportsByBoundingBox(SearchCriteria searchCriteria);
	public List<Airport> getAirportsByDistance(SearchCriteria searchCriteria);
	public List<Airport> getAirports();
	public Object getPlaces();
	public Object save(Place place) throws JsonProcessingException;
}