/**
 * 
 */
package com.fltck.demo.geospatial.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fltck.demo.geospatial.model.Place;
import com.fltck.demo.geospatial.service.RepositoryService;

/**
 * @author brentlemons
 *
 */
@RestController
@RequestMapping("/place")
public class PlaceController {
	
	@Value("${mapbox.key}")
	private String mapboxKey;
	
	@Value("${mapbox.uri}")
	private String mapboxUri;
	
    @Autowired
	RepositoryService repository;

    private static final Logger logger = LoggerFactory.getLogger(PlaceController.class);
    
	@RequestMapping(method = RequestMethod.POST)
	FeatureCollection find(@RequestBody Place place) {
		place.setLocation(findFirstLonLat(place));
		
		FeatureCollection featureCollection = new FeatureCollection();
		Feature feature = new Feature();
		
		feature.setProperty("name", place.getName());
		feature.setProperty("address", place.getAddress());			
		feature.setGeometry(place.getLocation());
		
		featureCollection.add(feature);
		
		return featureCollection;
	}

	@RequestMapping(method = RequestMethod.PUT)
	FeatureCollection persist(@RequestBody Place place) {
		FeatureCollection featureCollection = null;

		try {
			place.setLocation(findFirstLonLat(place));
		
			IndexResponse response = (IndexResponse) repository.save(place);
			
			if (response.getResult() != null) {
		
				featureCollection = new FeatureCollection();
				Feature feature = new Feature();
				
				feature.setProperty("name", place.getName());
				feature.setProperty("address", place.getAddress());			
				feature.setGeometry(place.getLocation());
				
				featureCollection.add(feature);
			}
		
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		}
		
		return featureCollection;
	}
	
	private Point findFirstLonLat(Place place) {
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("address", place.getAddress().toString());
		uriParams.put("mapboxToken", mapboxKey);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(mapboxUri);

		RestTemplate template = new RestTemplate();
		FeatureCollection mapboxFc = template.getForObject(builder.buildAndExpand(uriParams).toUri(), FeatureCollection.class);
		
		List<Feature> features = mapboxFc.getFeatures();
		Feature mbFeature = null;
		if (features.size() > 0) {
			mbFeature = features.get(0);
						
			return (Point) mbFeature.getGeometry();
		}
		return null;
	}

}
