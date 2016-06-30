package edu.pnu.project;

import java.util.ArrayList;
import java.util.List;

public class ProjectMetaData {
	private IndoorGMLIDRegistry idRegistry;
	private BuildingProperty buildingProperty;
	private double currentScale;
	private String currentFloorLevel;
	private List<Mapping> mappings;
	
	public IndoorGMLIDRegistry getIDRegistry() {
		return idRegistry;
	}
	
	public void setIDRegistry(IndoorGMLIDRegistry idRegistry) {
		this.idRegistry = idRegistry;
	}

	public BuildingProperty getBuildingProperty() {
		return buildingProperty;
	}

	public void setBuildingProperty(BuildingProperty buildingProperty) {
		this.buildingProperty = buildingProperty;
	}

	public double getCurrentScale() {
		return currentScale;
	}

	public void setCurrentScale(double currentScale) {
		this.currentScale = currentScale;
	}

	public String getCurrentFloorLevel() {
		return currentFloorLevel;
	}

	public void setCurrentFloorLevel(String currentFloorLevel) {
		this.currentFloorLevel = currentFloorLevel;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<Mapping> mappings) {
		this.mappings = mappings;
	}
	
	public void addMapping(Mapping mapping) {
		if(mappings == null) {
			mappings = new ArrayList<Mapping>();
		}
		
		mappings.add(mapping);
	}
}
