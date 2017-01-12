package com.fltck.demo.geospatial.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fltck.demo.geospatial.model.Airport;
import com.fltck.demo.geospatial.model.Place;
import com.fltck.demo.geospatial.model.SearchCriteria;
import com.fltck.demo.geospatial.service.RepositoryService;

public class ElasticsearchRepositoryServiceImpl implements RepositoryService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchRepositoryServiceImpl.class);
    private static String airportIndex = "airspace";
    private static String airportType = "airports";

	private Client elasticsearchClient;
	private String index;	
	private String type;

	public void setIndex(String index){
		this.index = index;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public ElasticsearchRepositoryServiceImpl(Client elasticsearchClient) {
		this.elasticsearchClient = elasticsearchClient;
	}
	
	@Override
	public FeatureCollection getPlaces() {
		return null;
	}

	@Override
	public Object save(Place place) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JodaModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		if (place.getId() == null) {
			place.setId(UUID.randomUUID().toString());
		}
		
		IndexResponse response = elasticsearchClient.prepareIndex("geospatial", "place", place.getId())
		        .setSource(mapper.writeValueAsBytes(place))
	        .get();
		return response;
	}
	
	@Override
	public List<Airport> getAirportsByBoundingBox(SearchCriteria searchCriteria) {
		List<Airport> airports = new ArrayList<Airport>();
		
		QueryBuilder qb = QueryBuilders.geoBoundingBoxQuery("geography.loc") 
			.setCorners(searchCriteria.getBoundingBox().getNorth(),
	    		searchCriteria.getBoundingBox().getWest(),
	    		searchCriteria.getBoundingBox().getSouth(),
	    		searchCriteria.getBoundingBox().getEast());
		SearchResponse response = elasticsearchClient.prepareSearch().setQuery(qb).setSize(searchCriteria.getMaxHits()).execute().actionGet();
		SearchHits hits = response.getHits();
		for (SearchHit hit : hits.getHits()) {
			airports.add(hitToAirport(hit));
		}
		
		return airports;
	}
	
	@Override
	public List<Airport> getAirportsByDistance(SearchCriteria searchCriteria) {
		List<Airport> airports = new ArrayList<Airport>();
		
		QueryBuilder qb = QueryBuilders.geoDistanceQuery("geography.loc") 
			.point(searchCriteria.getPoint().getCoordinates().getLatitude(), searchCriteria.getPoint().getCoordinates().getLongitude())
			.distance(searchCriteria.getDistance(), DistanceUnit.NAUTICALMILES);
		SearchResponse response = elasticsearchClient.prepareSearch().setQuery(qb).setSize(searchCriteria.getMaxHits()).execute().actionGet();
		SearchHits hits = response.getHits();
		for (SearchHit hit : hits.getHits()) {
			airports.add(hitToAirport(hit));
		}
		
		return airports;
	}
	
	@Override
	public List<Airport> getAirports() {
		List<Airport> airports = new ArrayList<Airport>();
		
		QueryBuilder qb = QueryBuilders.geoBoundingBoxQuery("geography.loc") 
				.setCorners(45.0,
		    		-140.0,
		    		20.0,
		    		-50.0);
			SearchResponse response = elasticsearchClient.prepareSearch().setQuery(qb).setSize(500).execute().actionGet();
		SearchHits hits = response.getHits();
		for (SearchHit hit : hits.getHits()) {
			airports.add(hitToAirport(hit));
		}
		
		return airports;
	}
	
	private Airport hitToAirport(SearchHit hit) {
		Map<String, Object> fields = hit.getSource();
		Airport airport = new Airport();
		airport.setId((String)fields.get("id"));
		airport.setIataId((String)fields.get("icaoCode"));
		airport.setName((String)fields.get("name"));
		airport.setCity((String)fields.get("city"));
		airport.setState((String)fields.get("state"));
		Map<String, Object> geography = (Map<String, Object>) fields.get("geography");
		Map<String, Object> location = (Map<String, Object>) geography.get("loc");
		airport.setLocation(new Point((double)location.get("lon"), (double)location.get("lat")));
		airport.setElevation((Double)geography.get("elevation"));

		return airport;
	}

	
}
