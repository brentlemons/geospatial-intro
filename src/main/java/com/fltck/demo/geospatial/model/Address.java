/**
 * 
 */
package com.fltck.demo.geospatial.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author brentlemons
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class Address {
	private String line1;
	private String line2;
	private String city;
	private String state;
	private String zipCode;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (line1 != null ? line1 + ", " : "")
				+ (line2 != null ? line2 + ", " : "") + (city != null ? city + ", " : "")
				+ (state != null ? state + ", " : "") + (zipCode != null ? zipCode : "");
	}
	
}
