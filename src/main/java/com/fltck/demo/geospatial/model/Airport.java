/**
 * 
 */
package com.fltck.demo.geospatial.model;

import org.geojson.Point;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author brentlemons
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class Airport {
	private String id;
	private String iataId;
	private String name;
	private String city;
	private String state;
	private Point location;
	private Double elevation;
}
