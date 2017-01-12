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
public class Place {
	private String id;
	private String name;
	private Address address;
	private Point location;
}
