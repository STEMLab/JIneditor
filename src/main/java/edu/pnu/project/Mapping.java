package edu.pnu.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mapping {
	private String level;
	private FloorProperty floorProperty;
	private Map<String, List<String>> map;
		
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public FloorProperty getFloorProperty() {
		return floorProperty;
	}

	public void setFloorProperty(FloorProperty floorProperty) {
		this.floorProperty = floorProperty;
	}
		
	public Map<String, List<String>> getMap() {
		return map;
	}
	
	public void setMap(Map<String, List<String>> map) {
		this.map = map;
	}

	public Mapping(String level, FloorProperty floorProperty) {
		this.level = level;
		this.floorProperty = floorProperty;
	}
	
	public void addMappedElement(String type, String element) {
		if(map == null) {
			map = new HashMap<String, List<String>>();
		}
		
		if(!map.containsKey(type)) {
			map.put(type, new ArrayList<String>());
		}
		
		map.get(type).add(element);
	}
	
	public boolean isContainsMappedElement(String type, String id) {
		if(map == null) {
			return false;
		}
		
		if(!map.containsKey(type)) {
			return false;
		}
		
		return map.get(type).contains(id);
	}
}
