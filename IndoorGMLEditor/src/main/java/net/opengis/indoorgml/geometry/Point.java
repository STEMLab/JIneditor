package net.opengis.indoorgml.geometry;

import java.io.Serializable;

import edu.pnu.visitor.IndoorGMLElement;
import edu.pnu.visitor.IndoorGMLElementVisitor;


public class Point extends AbstractGeometry implements Serializable, IndoorGMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -211650567038166362L;
	private static int labelNumber = 1;
	private double panelRatioX, panelRatioY;
	private double panelX, panelY, z;
	private double realX, realY;

	public Point() {
		super.setGMLID( "P" + (labelNumber++) );
	}
	
	public double getPanelX() {
		return panelX;
	}

	public void setPanelX(double x) {
		this.panelX = x;
	}

	public double getPanelY() {
		return panelY;
	}

	public void setPanelY(double y) {
		this.panelY = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public double getRealX() {
		return realX;
	}

	public void setRealX(double realX) {
		this.realX = realX;
	}

	public double getRealY() {
		return realY;
	}

	public void setRealY(double realY) {
		this.realY = realY;
	}

	public static int getLabelNumber() {
		return labelNumber;
	}

	public static void setLabelNumber(int labelNumber) {
		Point.labelNumber = labelNumber;
	}

	@Override
	public String toString() {
		return "(" + panelX + ", " + panelY + ", " + z + ")";
	}
	
	public void setXYZ(String pointToStringFormat){
		this.panelX = Double.parseDouble(pointToStringFormat.split(" ")[0]);
		this.panelY = Double.parseDouble(pointToStringFormat.split(" ")[1]);
		this.z = Double.parseDouble(pointToStringFormat.split(" ")[2]);
	}

	public double getPanelRatioX() {
		return panelRatioX;
	}

	public void setPanelRatioX(double panelRatioX) {
		this.panelRatioX = panelRatioX;
	}

	public double getPanelRatioY() {
		return panelRatioY;
	}

	public void setPanelRatioY(double panelRatioY) {
		this.panelRatioY = panelRatioY;
	}
	
	public void computeGMLCoordinate(int width, int height, double z) {
		this.panelX = panelRatioX * width;
		this.panelY = panelRatioY * height;
		this.z = z;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
	
	public Point clone() {
		Point point = new Point();
		
		point.panelRatioX = this.panelRatioX;
		point.panelRatioY = this.panelRatioY;
		point.panelX = this.panelX;
		point.panelY = this.panelY;
		point.z = this.z;
		
		return point;
	}
	
	public boolean equalsPanelRatioXY(Point other) {
		boolean isEquals = true;
		
		if(this.panelRatioX != other.panelRatioX) isEquals = false;
		if(this.panelRatioY != other.panelRatioY) isEquals = false;
		
		return isEquals;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	
}