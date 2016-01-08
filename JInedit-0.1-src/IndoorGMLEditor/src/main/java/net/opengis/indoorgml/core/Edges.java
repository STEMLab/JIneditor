package net.opengis.indoorgml.core;

import java.io.Serializable;
import java.util.ArrayList;

import edu.pnu.project.TransitionOnFloor;
import edu.pnu.visitor.IndoorGMLElement;
import edu.pnu.visitor.IndoorGMLElementVisitor;

public class Edges extends AbstractFeature implements Serializable, IndoorGMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6296215029048969109L;
	private static int labelNumber = 1;
	private ArrayList<TransitionOnFloor> transitionOnFloors;
	
	public Edges() {
		super.setGmlID( "E" + (labelNumber++) );
		transitionOnFloors = new ArrayList<TransitionOnFloor>();
	}

	public ArrayList<TransitionOnFloor> getTransitionOnFloors() {
		return transitionOnFloors;
	}

	public void setTransitionOnFloors(ArrayList<TransitionOnFloor> transitionOnFloor) {
		this.transitionOnFloors = transitionOnFloor;
	}

	public static int getLabelNumber() {
		return labelNumber;
	}

	public static void setLabelNumber(int labelNumber) {
		Edges.labelNumber = labelNumber;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
}