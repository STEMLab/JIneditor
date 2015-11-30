package net.opengis.indoorgml.geometry;

import java.io.Serializable;
import java.util.ArrayList;

import edu.pnu.visitor.IndoorGMLElement;
import edu.pnu.visitor.IndoorGMLElementVisitor;

public class LineString extends AbstractGeometry implements Serializable, IndoorGMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7910611962807310512L;
	private static int labelNumber = 1;
	private boolean isReversed;
	private ArrayList<Point> points;

	public LineString() {
		super.setGMLID( "LS" + (labelNumber++) );
		isReversed = false;
		points = new ArrayList<Point>();
		setxLinkGeometry(null);
	}
	
	public boolean getIsReversed() {
		return isReversed;
	}

	public void setIsReversed(boolean isReversed) {
		this.isReversed = isReversed;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points= points;
	}

	public static int getLabelNumber() {
		return labelNumber;
	}

	public static void setLabelNumber(int labelNumber) {
		LineString.labelNumber = labelNumber;
	}

	public LineString clone() {
		// TODO Auto-generated method stub
		LineString clone = new LineString();
		clone.setPoints((ArrayList<Point>) this.points.clone());
		return clone;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
}
