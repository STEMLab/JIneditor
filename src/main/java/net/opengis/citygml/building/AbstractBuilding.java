package net.opengis.citygml.building;

import java.util.ArrayList;
import java.util.List;

import edu.pnu.project.FloorProperty;

public class AbstractBuilding {
	private AbstractBuilding parent;
	private List<AbstractBuilding> buildingParts;
	private String type;
	
	private String gmlID;
	private String gmlName;
	private String clazz; // CodeType
	private String function; // CodeType
	private String usage; // CodeType
	private int yearOfConstruction;
	private int yearOfDemolition;
	private String roofType;
	private String measureHeight; // LengthType
	private int storeysAboveGround;
	private int storeysBelowGround;
	private String storeyHeightsAboveGround;
	private String storeyHeightsBelowGround;
	
	private List<FloorProperty> roofFloor;
	private List<FloorProperty> wallFloor;
	private List<FloorProperty> groundFloor;

	public AbstractBuilding() {
		yearOfConstruction = -1;
		yearOfDemolition = -1;
		storeysAboveGround = -1;
		storeysBelowGround = -1;		
	}

	public String getGmlID() {
		return gmlID;
	}

	public void setGmlID(String gmlID) {
		this.gmlID = gmlID;
	}

	public String getGmlName() {
		return gmlName;
	}

	public void setGmlName(String gmlName) {
		this.gmlName = gmlName;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public int getYearOfConstruction() {
		return yearOfConstruction;
	}

	public void setYearOfConstruction(int yearOfConstruction) {
		this.yearOfConstruction = yearOfConstruction;
	}

	public int getYearOfDemolition() {
		return yearOfDemolition;
	}

	public void setYearOfDemolition(int yearOfDemolition) {
		this.yearOfDemolition = yearOfDemolition;
	}

	public String getRoofType() {
		return roofType;
	}

	public void setRoofType(String roofType) {
		this.roofType = roofType;
	}

	public String getMeasureHeight() {
		return measureHeight;
	}

	public void setMeasureHeight(String measureHeight) {
		this.measureHeight = measureHeight;
	}

	public int getStoreysAboveGround() {
		return storeysAboveGround;
	}

	public void setStoreysAboveGround(int storeysAboveGround) {
		this.storeysAboveGround = storeysAboveGround;
	}

	public int getStoreysBelowGround() {
		return storeysBelowGround;
	}

	public void setStoreysBelowGround(int storeysBelowGround) {
		this.storeysBelowGround = storeysBelowGround;
	}

	public String getStoreyHeightsAboveGround() {
		return storeyHeightsAboveGround;
	}

	public void setStoreyHeightsAboveGround(String storeyHeightsAboveGround) {
		this.storeyHeightsAboveGround = storeyHeightsAboveGround;
	}

	public String getStoreyHeightsBelowGround() {
		return storeyHeightsBelowGround;
	}

	public void setStoreyHeightsBelowGround(String storeyHeightsBelowGround) {
		this.storeyHeightsBelowGround = storeyHeightsBelowGround;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<FloorProperty> getRoofFloor() {
		if (roofFloor == null) {
			roofFloor = new ArrayList<FloorProperty>();
		}
		return roofFloor;
	}

	public void setRoofFloor(List<FloorProperty> roofFloor) {
		this.roofFloor = roofFloor;
	}

	public List<FloorProperty> getWallFloor() {
		if (wallFloor == null) {
			wallFloor = new ArrayList<FloorProperty>();
		}
		return wallFloor;
	}

	public void setWallFloor(List<FloorProperty> wallFloor) {
		this.wallFloor = wallFloor;
	}

	public List<FloorProperty> getGroundFloor() {
		if (groundFloor == null) {
			groundFloor = new ArrayList<FloorProperty>();
		}
		return groundFloor;
	}

	public void setGroundFloor(List<FloorProperty> groundFloor) {
		this.groundFloor = groundFloor;
	}

	public AbstractBuilding getParent() {
		return parent;
	}

	public void setParent(AbstractBuilding parent) {
		this.parent = parent;
	}

	public List<AbstractBuilding> getBuildingParts() {
		if (buildingParts == null) {
			buildingParts = new ArrayList<AbstractBuilding>();
		}
		return buildingParts;
	}

	public void setBulidingParts(List<AbstractBuilding> bulidingParts) {
		this.buildingParts = bulidingParts;
	}
	
}
