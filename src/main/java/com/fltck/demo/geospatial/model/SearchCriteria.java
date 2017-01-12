/**
 * 
 */
package com.fltck.demo.geospatial.model;

import org.geojson.Point;

import lombok.Data;

/**
 * @author brentlemons
 *
 */
@Data
public class SearchCriteria {
	private Integer maxHits;
	private BoundingBox boundingBox;
	private Point point;
	private Double distance;
}
