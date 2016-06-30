package edu.pnu.project;

import java.io.Serializable;

import net.opengis.indoorgml.geometry.Point;

public class FloorProperty implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6976018125425150806L;
	private String level;
	private Point bottomLeftPoint;
	private Point topRightPoint;
	private double groundHeight;
	private double ceilingHeight;
	private double doorHeight;
	private String floorPlanPath;

	public FloorProperty() {
		// TODO Auto-generated constructor stub
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Point getBottomLeftPoint() {
		return bottomLeftPoint;
	}

	public void setBottomLeftPoint(Point bottomLeftPoint) {
		this.bottomLeftPoint = bottomLeftPoint;
	}

	public Point getTopRightPoint() {
		return topRightPoint;
	}

	public void setTopRightPoint(Point topRightPoint) {
		this.topRightPoint = topRightPoint;
	}

	public double getGroundHeight() {
		return groundHeight;
	}

	public void setGroundHeight(double groundHeight) {
		this.groundHeight = groundHeight;
	}

	public double getCeilingHeight() {
		return ceilingHeight;
	}

	public void setCeilingHeight(double ceilingHeight) {
		this.ceilingHeight = ceilingHeight;
	}

	public double getDoorHeight() {
		return doorHeight;
	}

	public void setDoorHeight(double doorHeight) {
		this.doorHeight = doorHeight;
	}

	public String getFloorPlanPath() {
		return floorPlanPath;
	}

	public void setFloorPlanPath(String floorPlanPath) {
		this.floorPlanPath = floorPlanPath;
	}

}
