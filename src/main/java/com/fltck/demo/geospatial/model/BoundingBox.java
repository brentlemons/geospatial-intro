/**
 * 
 */
package com.fltck.demo.geospatial.model;

import lombok.Data;

/**
 * @author brentlemons
 *
 */
@Data
public class BoundingBox {
	private Double north;
	private Double west;
	private Double south;
	private Double east;
}
