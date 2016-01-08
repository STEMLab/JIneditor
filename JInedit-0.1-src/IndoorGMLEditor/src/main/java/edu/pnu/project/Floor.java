package edu.pnu.project;

import java.io.Serializable;

public class Floor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5198512369801729700L;
	private FloorProperty floorProperty;
	
	public Floor() {
		// TODO Auto-generated constructor stub
	}

	public FloorProperty getFloorProperty() {
		return floorProperty;
	}

	public void setFloorProperty(FloorProperty floorProperty) {
		this.floorProperty = floorProperty;
	}

}
