package edu.pnu.project;

import java.io.Serializable;
import java.util.ArrayList;

public class BuildingProperty implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 59525300782083537L;
	private ArrayList<FloorProperty> floorProperties;

	public BuildingProperty() {
		// TODO Auto-generated constructor stub
		floorProperties = new ArrayList<FloorProperty>();
	}

	public ArrayList<FloorProperty> getFloorProperties() {
		return floorProperties;
	}

	public void setFloorProperties(ArrayList<FloorProperty> floorProperties) {
		this.floorProperties = floorProperties;
	}
	
	public FloorProperty getFloorProperty(String level) {
		for(FloorProperty floorProperty : floorProperties) {
			if(floorProperty.getLevel().equals(level)) {
				return floorProperty;
			}
		}
		
		return null;
	}

}
