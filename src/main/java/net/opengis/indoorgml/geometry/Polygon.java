package net.opengis.indoorgml.geometry;

import java.io.Serializable;
import java.util.ArrayList;

import edu.pnu.visitor.IndoorGMLElementVisitor;

public class Polygon extends AbstractGeometry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2078126302577654879L;
	private static int labelNumber = 1;
	private boolean isReversed;
	private LinearRing exteriorRing;
	private ArrayList<LinearRing> interiorRing;
	
	public Polygon() {
		super.setGMLID( "POLY" + (labelNumber++) );
		interiorRing = new ArrayList<LinearRing>();
		isReversed = false;
		setxLinkGeometry(null);
	}
	
	public boolean getIsReversed() {
		return isReversed;
	}

	public void setIsReversed(boolean isReversed) {
		this.isReversed = isReversed;
	}

	public LinearRing getExteriorRing() {
		return exteriorRing;
	}
	
	public void setExteriorRing(LinearRing exteriorRing) {
		this.exteriorRing = exteriorRing;
	}
	
	public ArrayList<LinearRing> getInteriorRing() {
		return interiorRing;
	}
	
	public void setInteriorRing(ArrayList<LinearRing> interiorRing) {
		this.interiorRing = interiorRing;
	}
	
	public static int getLabelNumber() {
		return labelNumber;
	}
	
	public static void setLabelNumber(int labelNumber) {
		Polygon.labelNumber = labelNumber;
	}
	
	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		//visitor.visit(this);
	}

	@Override
	public Polygon clone() {
		Polygon clone = new Polygon();
		clone.setGMLID(this.getGMLID());
		
		LinearRing exteriorClone = exteriorRing.clone();
		ArrayList<LinearRing> interiorClone = clone.getInteriorRing();
		for (LinearRing interior : interiorRing) {
			interiorClone.add(interior.clone());
		}
		clone.setExteriorRing(exteriorClone);
		
		return clone;
	}
	
	
}